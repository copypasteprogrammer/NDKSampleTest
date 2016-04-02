

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
    LOCAL_MODULE := GTTknP11
    LOCAL_SRC_FILES := libGTTknP11.so
    LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
    LOCAL_MODULE := ndkLib
    LOCAL_SRC_FILES := main.c \
                       funkcijos.c \
    LOCAL_SHARED_LIBRARIES := GTTknP11
     LOCAL_LDLIBS := libGTTknP11.so -llog
include $(BUILD_SHARED_LIBRARY)