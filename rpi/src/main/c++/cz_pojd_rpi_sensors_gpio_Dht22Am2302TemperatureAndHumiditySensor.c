#include <jni.h>
#include <stdio.h>
#include "common_dht_read.h"
#include "pi_dht_read.h"
#include "cz_pojd_rpi_sensors_gpio_Dht22Am2302TemperatureAndHumiditySensor.h"

JNIEXPORT jdoubleArray JNICALL Java_cz_pojd_rpi_sensors_gpio_Dht22Am2302TemperatureAndHumiditySensor_executeNative(
                JNIEnv *env, jobject thisObject, jint pin) {

        float temperature, humidity;
        jdoubleArray result;

        int callResult = DHT_ERROR_TIMEOUT;
        for (int attempt = 1; callResult != DHT_SUCCESS && attempt<=10; attempt++) {
                if (attempt > 1) {
                        sleep_milliseconds(500);
                }
                callResult = pi_dht_read(DHT22, pin, &humidity, &temperature);
        }

        if (callResult == DHT_SUCCESS) {
                double cArray[2] = { temperature, humidity };
                result = (*env)->NewDoubleArray(env, 2);
                (*env)->SetDoubleArrayRegion(env, result, 0, 2, cArray);
                return result;
        } else {
                printf("Error reading value from sensor, error code: %d", callResult);
        }
        return NULL;
}
