package com.triadss.doctrack2.utils;

import androidx.health.connect.client.records.HeartRateRecord;

import kotlin.reflect.KClass;

public class HealthConnectUtils {
    public static KClass<HeartRateRecord> getHeartRateRecordClass()
    {
        return kotlin.jvm.JvmClassMappingKt.getKotlinClass(HeartRateRecord.class);
    }
}
