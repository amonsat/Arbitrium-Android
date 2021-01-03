LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

LOCAL_MODULE := runFrame
LOCAL_LDFLAGS += -static
LOCAL_SRC_FILES := runFrame.c

LOCAL_CFLAGS += -DDEBUG

include $(BUILD_EXECUTABLE)


include $(CLEAR_VARS)

LOCAL_SRC_FILES:=\
	toolbox.c


LOCAL_CFLAGS:= \
    -O2 -g \
    -Wall -Werror \
    -Wno-deprecated-declarations \
    -Wno-sign-compare \
    -Wno-unused-variable \


LOCAL_MODULE_TAGS := eng
LOCAL_LDFLAGS += -static
LOCAL_MODULE_PATH := $(TARGET_OUT_OPTIONAL_EXECUTABLES)

LOCAL_MODULE:=toolbox

LOCAL_LDFLAGS += -Wl,--no-fatal-warnings

include $(BUILD_EXECUTABLE)
