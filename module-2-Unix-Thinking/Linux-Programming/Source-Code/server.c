#define _XOPEN_SOURCE 700
 
 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
 
 
#include <arpa/inet.h>
#include <fcntl.h>
#include <netdb.h>                      /* getprotobyname */
#include <netinet/in.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <unistd.h>
 
 
#include <pthread.h>
 
 
struct client {
    socklen_t client_len;
    struct sockaddr_in client_address;
    int client_sockfd;
    pthread_t thread;
};
 
 
 
 
// NOTE: provide enough space for a 5 digit port + EOS char
enum { PORTSIZE = 6 };
 
 
double cpu_time_used;
clock_t start, end;
 
 
 
 
void *forClient(void *ptr);
 
 
void portCleaner(const char* port_num) {
    char temp[100] = "sudo lsof -t -i tcp:";
    sprintf(temp, "%s%s%s", temp, port_num, " | xargs kill -9;");
    system(temp);
    //puts(temp);
}
 
 
void
sig_handler(int signo)
{
    if (signo == SIGINT)
        printf("!!  OUCH,  CTRL - C received  by server !!\n");
}
 
 
int
main(int argc, char **argv)
{
    struct addrinfo hints,
    *res;
    int enable = 1;
    //int filefd;  // NOTE: this is never initialized/used
    int server_sockfd;
    unsigned short server_port = 12345u;
    char portNum[PORTSIZE];
    struct sockaddr_in server_address;
    struct protoent *protoent;
    char protoname[] = "tcp";
 
 
 
 
     
#if 0
    int socket_index = 0;
#else
    struct client *ctl;
#endif
     
    if (argc != 2) {
        fprintf(stderr, "Usage   ./server  <port>\n");
        exit(EXIT_FAILURE);
    }
    server_port = strtol(argv[1], NULL, 10);
     
    /* Create a socket and listen to it.. */
    protoent = getprotobyname(protoname);
    if (protoent == NULL) {
        perror("getprotobyname");
        exit(EXIT_FAILURE);
    }
    server_sockfd = socket(
                           AF_INET,
                           SOCK_STREAM,
                           protoent->p_proto
                           );
    if (server_sockfd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    if (setsockopt(server_sockfd, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(enable)) < 0) {
        perror("setsockopt(SO_REUSEADDR) failed");
        exit(EXIT_FAILURE);
    }
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(server_port);
    if (bind(
             server_sockfd,
             (struct sockaddr*)&server_address,
             sizeof(server_address)
             ) == -1
        ) {
        perror("bind");
        portCleaner(argv[1]);
        exit(EXIT_FAILURE);
    }
    if (listen(server_sockfd, 5) == -1) {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    fprintf(stderr, "listening on port %d\n", server_port);
     
 
 
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr,1);
     
    start = clock();
     
    while (1) {
 
 
 
 
        ctl = malloc(sizeof(struct client));
        if (ctl == NULL) {
            perror("malloc");
            exit(EXIT_FAILURE);
        }
         
        ctl->client_len = sizeof(ctl->client_address);
        puts("waiting for client");
         
        ctl->client_sockfd = accept(server_sockfd,
                                    (struct sockaddr *) &ctl->client_address, &ctl->client_len);
         
        if (ctl->client_sockfd < 0) {
            perror("Cannot accept connection\n");
            close(server_sockfd);
            exit(EXIT_FAILURE);
        }
 
 
        pthread_create(&ctl->thread, &attr, forClient, ctl);
 
 
    }
     
    return EXIT_SUCCESS;
}
 
 
void *
forClient(void *ptr)
{
     
    end = clock();
    cpu_time_used = 1000 * (((double) (end - start)) / CLOCKS_PER_SEC);
#if 0
    int connect_socket = (int) ptr;
#else
    struct client *ctl = ptr;
    int connect_socket = ctl->client_sockfd;
#endif
    int filefd;
    ssize_t read_return;
    char buffer[BUFSIZ];
    char *file_path;
    char receiveFileName[BUFSIZ];
    char cmd[BUFSIZ];
 
 
    // Thread number means client's id
    printf("Connected time  [%lf] ---  Thread number [%ld]\n", cpu_time_used, pthread_self());
     
 
 
     
    // until stop receiving go on taking information
    while (recv(connect_socket, receiveFileName, sizeof(receiveFileName), 0)) {
 
 
        if((strcmp(receiveFileName, "listServer") == 0
           || strcmp(receiveFileName, "listLocal") == 0 || strcmp(receiveFileName, "help") == 0
            || strcmp(receiveFileName, "exit") == 0 || strcmp(receiveFileName, "sendFile") == 0)) {
            printf("--- Command <%s> ---\n", receiveFileName);
            continue;
        }
         
        file_path = receiveFileName;
         
        fprintf(stderr, "is the file name received? ?   =>  %s\n", file_path);
         
        filefd = open(file_path, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
        if (filefd == -1) {
            perror("open");
            exit(EXIT_FAILURE);
        }
        do {
            read_return = read(connect_socket, buffer, BUFSIZ);
            if (read_return == -1) {
                perror("read");
                exit(EXIT_FAILURE);
            }
            if (write(filefd, buffer, read_return) == -1) {
                perror("write");
                exit(EXIT_FAILURE);
            }
        } while (read_return > 0);
         
        // NOTE/BUG: filefd was never closed
        close(filefd);
         
    }
     
    fprintf(stderr, "Client dropped connection\n");
     
    // NOTE: do all client related cleanup here
    // previously, the main thread was doing the close, which is why it had
    // to do the pthread_join
    close(connect_socket);
    free(ctl);
     
    return (void *) 0;
}
