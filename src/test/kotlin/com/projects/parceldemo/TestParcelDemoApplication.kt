package com.projects.parceldemo

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<ParcelDemoApplication>().with(TestcontainersConfiguration::class).run(*args)
}
