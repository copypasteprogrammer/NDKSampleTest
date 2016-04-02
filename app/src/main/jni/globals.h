#ifndef ___GLOBALS_DOT_H___
#define ___GLOBALS_DOT_H___

#ifdef WIN32

#include "cryptoki-win32.h"

#else

#include "cryptoki.h"
typedef long BOOL;

#endif


//*************************************************************************
//define this to re-direct the output to a file rather than standard output
//#define _TEST_

//*************************************************************************

#define _TEST_

#ifdef _TEST_
	#define STDERR stderr
#else
	#define STDERR stdout
#endif


#ifdef _TEST_

#ifdef WIN32
// for WIN32
#include <windows.h>
SYSTEMTIME st;
#define TESTINITIALIZE	fprintf(STDERR, "========================================================\n"); \
							fprintf(STDERR, "\n\nProject Name: %s - Date: %d/%d/%d - Time: %02d:%02d\n",  \
									ProjName, st.wMonth, st.wDay, st.wYear, st.wHour, st.wMinute); \
						fflush(STDERR);

#define TESTFINALIZE	if(rv == 0) \
							fprintf(STDERR, "%s finished successfully.\n", ProjName); \
						else \
							fprintf(STDERR, "%s failed: 0x%08x\n", ProjName, rv);
#else
// for Linux
#include <stdio.h>
#define TESTINITIALIZE	fprintf(STDERR, "========================================================\n"); \
						fprintf(STDERR, "\n\nProject Name: %s\n", ProjName ); \
						fflush(STDERR);

#define TESTFINALIZE	if(rv == 0) \
							fprintf(STDERR, "%s finished successfully.\n", ProjName); \
						else \
							fprintf(STDERR, "%s failed: 0x%08x\n", ProjName, rv);
#endif

#else	// non TEST

#define TESTINITIALIZE
#define TESTFINALIZE

#endif

extern char * SOpin;
extern char * userPin;

#endif	// ___GLOBALS_DOT_H___
