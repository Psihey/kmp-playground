package di

import org.koin.dsl.module
import screen.BirdsViewModel

val viewmodel = module{
    factory {
        BirdsViewModel(get())
    }
}