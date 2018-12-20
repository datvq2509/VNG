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
 
 
// NOTE/BUG: this didn't provide enough space for a 5 digit port + EOS char
#if 0
enum { PORTSIZE = 5 };
#else
enum { PORTSIZE = 6 };
#endif
 
 
void
sig_handler(int signo)
{
    if (signo == SIGINT)
        printf("!!  OUCH,  CTRL - C received on client  !!\n");
}
 
 
int
main(int argc, char **argv)
{
    struct addrinfo hints,
    *res;
    char *server_hostname = "127.0.0.1";
    char file_path[BUFSIZ];
    char *server_reply = NULL;
    char *user_input = NULL;
    char buffer[BUFSIZ];
    int filefd;
    int sockfd;
    ssize_t read_return;
    struct hostent *hostent;
    unsigned short server_port = 12345;
    char portNum[PORTSIZE];
    char remote_file[BUFSIZ];
    int select;
    char *client_server_files[BUFSIZ];
    int i = 0;
    int j;
    char protoname[] = "tcp";
    struct protoent *protoent;
    struct sockaddr_in sockaddr_in;
    in_addr_t in_addr;
 
 
     
    // char filename_to_send[BUFSIZ];
     
    if (argc != 3) {
        fprintf(stderr, "Usage   ./client  <ip>  <port>\n");
        exit(EXIT_FAILURE);
    }
     
    server_hostname = argv[1];
    server_port = strtol(argv[2], NULL, 10);
     
     
    /* Get socket. */
    protoent = getprotobyname(protoname);
    if (protoent == NULL) {
        perror("getprotobyname");
        exit(EXIT_FAILURE);
    }
    sockfd = socket(AF_INET, SOCK_STREAM, protoent->p_proto);
    if (sockfd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    /* Prepare sockaddr_in. */
    hostent = gethostbyname(server_hostname);
    if (hostent == NULL) {
        fprintf(stderr, "error: gethostbyname(\"%s\")\n", server_hostname);
        exit(EXIT_FAILURE);
    }
    in_addr = inet_addr(inet_ntoa(*(struct in_addr*)*(hostent->h_addr_list)));
    if (in_addr == (in_addr_t)-1) {
        fprintf(stderr, "error: inet_addr(\"%s\")\n", *(hostent->h_addr_list));
        exit(EXIT_FAILURE);
    }
    sockaddr_in.sin_addr.s_addr = in_addr;
    sockaddr_in.sin_family = AF_INET;
    sockaddr_in.sin_port = htons(server_port);
     
    /* Do the actual connection. */
    if (connect(sockfd, (struct sockaddr*)&sockaddr_in, sizeof(sockaddr_in)) == -1) {
        perror("connect");
        return EXIT_FAILURE;
    }
    while (1) {
        if (signal(SIGINT, sig_handler)) {
            break;
        }
         
        puts("connected to the server");
        puts("-----------------");
        puts("|1 - listLocal| \n|2 - listServer| \n|3 - sendFile| \n|4 - help| \n|5 - exit| ");
        puts("-----------------");
        while (1) {
            printf("------%d",select);
            scanf("%d", &select);
            while ( getchar() != '\n' );
 
 
             
            switch (select) {
                case 1: // list files of client's directory
                    system("find . -maxdepth 1 -type f | sort");
                    sprintf(remote_file, "%s", "listLocal");
                    send(sockfd, remote_file, sizeof(remote_file), 0);
                    break;
                     
                case 2: // listServer
                    sprintf(remote_file, "%s", "listServer");
                    send(sockfd, remote_file, sizeof(remote_file), 0);
                    puts("---- Files btw Server and the Client ----");
                    for (j = 0; j < i; ++j) {
                        puts(client_server_files[j]);
                    }
                    break;
                     
                case 3: // send file
                    memset(file_path, 0, sizeof file_path);
                    scanf("%s", file_path);
                     
                    sprintf(remote_file, "%s", "sendFile");
                    send(sockfd, remote_file, sizeof(remote_file), 0);
                     
                    memset(remote_file, 0, sizeof remote_file);
                    // send file name to server
                    sprintf(remote_file, "%s", file_path);
                    send(sockfd, remote_file, sizeof(remote_file), 0);
                     
                    filefd = open(file_path, O_RDONLY);
                    if (filefd == -1) {
                        perror("open send file");
                        //exit(EXIT_FAILURE);
                        break;
                    }
                     
                    while (1) {
                        read_return = read(filefd, buffer, BUFSIZ);
                        if (read_return == 0)
                            break;
                        if (read_return == -1) {
                            perror("read");
                            //exit(EXIT_FAILURE);
                            break;
                        }
                        if (write(sockfd, buffer, read_return) == -1) {
                            perror("write");
                            //exit(EXIT_FAILURE);
                            break;
                        }
                    }
                     
                    // add files in char pointer array
                    client_server_files[i++] = file_path;
                     
                    close(filefd);
                    break;
                     
                case 5:
                    sprintf(remote_file, "%s", "exit");
                    send(sockfd, remote_file, sizeof(remote_file), 0);
                    free(user_input);
                    free(server_reply);
                    exit(EXIT_SUCCESS);
                     
                default:
                    puts("Wrong selection!");
                    break;
            }
 
 
        }
    }
     
    free(user_input);
    free(server_reply);
    exit(EXIT_SUCCESS);
}
