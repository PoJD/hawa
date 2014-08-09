gcc -std=gnu99 -Wall -fPIC -I/opt/oracle-jdk-bin-1.7.0.60/include/ -I/opt/oracle-jdk-bin-1.7.0.60/include/linux -c *.c
gcc -shared -o libdht.so *.o
rm -rf *.o
