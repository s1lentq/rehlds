#include "precompiled.h"
#include "rehlds_tests_shared.h"
#include "cppunitlite/MainAdapter.h"

int main(int argc, char* argv[]) {
	printf("TestRunner: main()\n");

	MainAdapter a;
	int res = a.testsEntryPoint(argc, argv);

#ifdef _BUILD_FROM_IDE
	system("PAUSE");
#endif

	return res;
}
