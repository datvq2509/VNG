# Debugging

Ví dụ chúng ta có file mã thực thi là demo.cpp có nội dung như sau :

```
// demo.cpp

#include <iostream>
int main()
{
 	int n;
 	n = abc;
return 0;
}
```
sau đó build chương trình với :
Ngôn ngữ C thì dùng câu lệnh :
```
gcc demo.cpp -o demo
```
Hoặc
```
clang demo.cpp -o demo
```

Ngôn ngữ C++ thì dùng câu lệnh :
```
g++ demo.cpp -o demo
```
-o là gì theo như mặc định khi không có -o chương trình sẽ xuất kết quả sau khi compile ra 1 file có tên là a.out mặc định là như vậy -o là để không xuất ra file đó mà chỉ xuất ra file chúng ta đã chỉ định.
- Lưu ý : g++ có thể build được file .c nhưng gcc không build được file .cpp tức là g++ có thể dùng cho cả c và c++ còn gcc chỉ dùng được cho c.



 Sau đó chương trình sẽ được compile nếu code không lỗi và thành công thì kết quả sẽ được xuất ra file demo.out muốn xem kết quả chúng ta dùng câu lệnh ./demo để xem kết quả nếu code lỗi chương trình sẽ báo ra lỗi cho chúng ta.


# Build System

Cách make file trong C/C++
- Tại sao phải make file ?
 Như demo ở bên trên với chương trình đơn giản thì chúng ta chỉ cần compile và debug trên duy nhất 1 file nhưng trường hợp chương trình của chúng ta có nhiều file thì việc debug va compile sẽ rất phức tạp vì vậy chúng ta cần makefile để chương trình hiểu luồng xử lý thế nào cho chính xác.
- make 

```
all :
g++ -Wall func1.c func2.c func3.c main.c -o func

// -Wall là để in ra tất cả các cảnh báo khi thực hiện chương trình chẳng hạn cảnh báo over threading ,etc...
```

- make test // xem kết quả sau khi make file.

Đơn giản 

```

exec: 
./func 

```

Tức executable ./func là thực hiện kết quả của file biên dịch ra được
- make clean
Đơn giản chỉ là dọn dẹp sau khi chương trình ra kết quả và chạy xong cảm thấy file nào cần phải xóa thì :

```
clean :
rm -rf *.o exe
```

Vậy là xong make , make test , make clean
// That was EZ
# Memory management
- Pages and Paging
Bộ nhớ thì bao gồm các bit các bit sẽ tạo thành bytes và byte thì chứa các words (từ) đó là đơn vị nhỏ nhất mà MMU (Memory management unit) có thể quản lỳ được.
- Sharing & Copy on write
Khi các thred sinh ra tất cả sẽ dùng chung 1 resource hay vùng nhớ (share) thì khi chạy chương trình các thread không được làm thay đỗi giá trị trên vùng nhớ gốc này mà phải copy (Copy on write) thành một bản khác rồi sau đó muốn thay đổi chỉnh sửa gì thì tùy biến nôm na như truyền tham chiếu và truyền tham trị trong lập trình C/C++.

- Memory Regions
    + Text segment : Chứa các dòng lệnh của chương trình.
    + Stack segment : Chứa địa chỉ trả về của hàm, các biến cục bộ được khai báo trong thân hàm.
    + Heap segment : Chứa vùng nhớ được cấp phát động
    + Bss segment : Chứa các biến chưa được khởi tạo.
- Allocating Dynamic Memory
    + Được sử dụng để cấp vùng nhớ cho các biến 

Một số câu lệnh xin cấp phát vùng nhớ 

```
void * malloc (size_t size);
void * calloc (size_t nr, size_t size);
void * realloc (void *ptr, size_t size);

- Resize kích thước con trỏ *p đang nắm giữ theo yêu cầu và sao chép lại các gía trị trong vùng nhớ cũ.

```



Và đương nhiên sau khi cấp phát chúng ta phải xin hủy để trả lại vùng nhớ cho máy


```
void free (void *ptr);
```

Với C++
Cấp phát : 

```

- void* operator new[] (std::size_t size);
- MyClass * p1 = new MyClass[5];

```


Hủy vùng nhớ : 



```
void operator delete[] (void* ptr) noexcept;
```

Example :

  ```
  MyClass * pt;
  pt = new MyClass[3];
  delete[] pt;
  ```





# Networking
Chúng ta có cơ chế như sau :

- SOCKET : Create a new communication end point.
- BIND : Attach a local address to a socket
- LISTEN : Announce willingness to accept connections; give queue size
- ACCEPT : Block the caller until connection attempt arrives
- CONNECT : Actively attempt to establish a connection
- SEND : Send some data over the connection
- RECEIVE : Receive some data from the connection
- CLOSE : Release the connection

Tại server chúng ta có workflow như sau :

- socket() -> bind() -> listen() -> accpet() -> send() -> receiv().

Tại client chúng ta có workflow như sau:
- socket() -> connect() -> send()-> receiv().

## Trong đó:
    - socket() có nghĩa là chúng ta tạo ra 1 socket cho việc kết nối.
    - bind() ấn định port sẽ liên lạc trong suốt process của socket đó.
    - listen() chờ đợi connection request từ client.
    - accpept() đồng ý kết nối vời client qua port tương thích.

Theo workflow đó chương trình socket kết nối server và clients như sau :

- Tại Server .

```
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
```

Tại Client 
```
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
```
# Multi-Threading
- POSIX THREAD :
POSIX Thread là thư viện hỗ trợ việc tạo và quản lý các thread, tận dụng sức mạnh của bộ xử lý trung tâm (CPU).

- Các thread cơ bản :

    + thread creation. 
    + terminatio. 
    + synchronization (join, blocking).
    + scheduling.
    + data management.
    + process interaction.

- Mutex :
    + Mutex giúp bảo vệ các vùng dữ liệu được shared, mutex quyết định liệu thread đi vào vùng nhớ chia sẻ đó hay không. Mutex chỉ được áp dụng cho các thread trong cùng một process với nhau.
    + Pros : giúp đảm bảo tính nhất quán của dữ liệu được chia sẻ giữa các thread.
    + Cons : chương trình sẽ trở nên chậm do tồn tại các điểm bottle-neck, có nguy cơ cao dẫn đến deadlock.