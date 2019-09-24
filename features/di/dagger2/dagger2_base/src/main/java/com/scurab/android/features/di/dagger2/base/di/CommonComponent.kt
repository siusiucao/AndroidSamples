package com.scurab.android.features.di.dagger2.base.di

import dagger.Subcomponent

@Subcomponent
interface CommonComponent : DIComponent {
    /*
        inject common components used everywhere
        for example Views/PickerDialogs etc...
     */
}

interface CommonComponentProvider : DIComponentProvider {
    fun commonComponent(): CommonComponent
}