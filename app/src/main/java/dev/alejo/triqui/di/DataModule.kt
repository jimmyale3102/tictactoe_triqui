package dev.alejo.triqui.di

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alejo.triqui.data.network.FirebaseService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabaseReference() = Firebase.database.reference

    @Provides
    @Singleton
    fun provideFirebaseService(databaseReference: DatabaseReference) =
        FirebaseService(databaseReference)

}