package com.projects.parceldemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParcelDemoApplication

fun main(args: Array<String>) {
    runApplication<ParcelDemoApplication>(*args)
}
