package com.scurab.android.features.di.dagger2.base

import dagger.Subcomponent

@Subcomponent
interface CommonComponent : DIComponent

interface CommonComponentProvider : DIComponentProvider {
    fun commonComponent(): CommonComponent
}