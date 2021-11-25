package com.richmat.mytuya.di

import android.content.Context
import androidx.room.Room
import com.richmat.mytuya.data.data_source.UserDataBase
import com.richmat.mytuya.data.repository.UserRepositoryImpl
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.richmat.mytuya.ui.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesUserDatabase(@ApplicationContext context: Context): UserDataBase {
        return Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            UserDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: UserDataBase): UserRepository {
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun provideUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCase(
            getUser = GetUser(repository),
            getUsers = GetUsers(repository),
            login = Login(repository),
            insertUser = InsertUser(repository),
            forgetLogin = ForgetLogin(repository),
            sendVerifyCode = SendVerifyCode(repository),
            getCountries = GetCountries(repository),
            observeCountry = ObserveCountry(repository),
            selectedCurrentCountry = SelectedCurrentCountry(repository),
            register = Register(repository),
            touristRegisterAndLogin = TouristRegisterAndLogin(repository)
        )
    }
}