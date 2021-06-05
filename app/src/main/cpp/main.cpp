#include "logging.h"
#include "native_api.h"
#include <dlfcn.h>

static HookFunType hook_func = nullptr;

bool hasEnding(std::string_view fullString, std::string_view ending) {
    if (fullString.length() >= ending.length()) {
        return (0 == fullString.compare(fullString.length() - ending.length(), ending.length(),
                                        ending));
    } else {
        return false;
    }
}

std::string (*originGetFCFlag)();

std::string (fakeGetFCFlag)() {
    return "10";
}

int (*originHasFC)();

int (fakeHasFC)() {
    return 0;
}

void on_library_loaded(const char *name, void *handle) {
    if (hasEnding(name, "libnative-lib.so")) {
        void *hasFC = dlsym(handle, "_Z5hasFCv");
        void *getFCFlag = dlsym(handle, "_Z9getFCFlagv");
        hook_func(hasFC, (void *) fakeHasFC, (void **) &originHasFC);
        hook_func(getFCFlag, (void *) fakeGetFCFlag, (void **) &originGetFCFlag);
    }
}

extern "C" __attribute__((visibility("default"))) NativeOnModuleLoaded
native_init(const NativeAPIEntries *entries) {
    hook_func = entries->hookFunc;
    return on_library_loaded;
}
