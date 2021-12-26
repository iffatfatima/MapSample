package com.app.mapsample.domain.base

abstract class Mapper<in T,E>{

    abstract fun mapFrom(from: T): E
}