cflatlib:
	gcc -m32 -c -Wall -Werror -fpic ificflatlib.c
	gcc -m32 -shared -o libcflat.so ificflatlib.o

clean:
	rm -rf *.o *.so
