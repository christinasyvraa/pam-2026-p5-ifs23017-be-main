package org.delcom

import io.ktor.server.application.Application
import org.ifs23017.module as ifsModule

/**
 * Compatibility shim:
 * - Sebelumnya mainClass / run config menunjuk ke `org.delcom.ApplicationKt`.
 * - Setelah refactor package ke `org.ifs23017`, sebagian environment (mis. IntelliJ Run Configuration lama)
 *   masih mencoba menjalankan class lama itu dan gagal `ClassNotFoundException`.
 *
 * File ini menjaga backward-compatibility dengan mendelegasikan ke entrypoint baru.
 */
fun main(args: Array<String>) {
    org.ifs23017.main(args)
}

/**
 * Opsional tapi berguna: kalau ada config lama (application.yaml) yang masih menunjuk ke
 * `org.delcom.ApplicationKt.module`, ini tetap akan bekerja.
 */
fun Application.module() {
    ifsModule()
}
