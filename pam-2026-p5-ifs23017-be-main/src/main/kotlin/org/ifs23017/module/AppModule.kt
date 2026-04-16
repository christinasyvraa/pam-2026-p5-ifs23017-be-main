package org.ifs23017.module

import org.ifs23017.repositories.*
import org.ifs23017.services.AuthService
import org.ifs23017.services.TodoService
import org.ifs23017.services.UserService
import org.koin.dsl.module

fun appModule(jwtSecret: String) = module {
    // User Repository
    single<IUserRepository> {
        UserRepository()
    }

    // User Service
    single {
        UserService(get(),get())
    }

    // Refresh Token Repository
    single<IRefreshTokenRepository> {
        RefreshTokenRepository()
    }

    // Auth Service
    single {
        AuthService(jwtSecret,get(), get())
    }

    // Plant Repository
    single<ITodoRepository> {
        TodoRepository()
    }

    // Plant Service
    single {
        TodoService(get(),get())
    }
}
