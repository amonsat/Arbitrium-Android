ARCH := all
SDK_VERSION := 21

all: build


build:
	ndk-build NDK_PROJECT_PATH=. APP_BUILD_SCRIPT=./Android.mk APP_ABI=$(ARCH) APP_PLATFORM=android-$(SDK_VERSION)


clean:
	rm -rf libs
	rm -rf obj

