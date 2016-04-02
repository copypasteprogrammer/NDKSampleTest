#include "funkcijos.h"

int foo(char* name) {
   //do stuff
   char siaip[]={"e"};
   memcpy(name,&siaip,sizeof(siaip));
   return 0;

}