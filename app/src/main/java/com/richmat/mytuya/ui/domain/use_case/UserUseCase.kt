package com.richmat.mytuya.ui.domain.use_case

data class UserUseCase(
    val getUsers: GetUsers,
    val getUserById: GetUserById,
    val login: Login,
    val insertUser: InsertUser,
    val forgetLogin: ForgetLogin,
    val sendVerifyCode: SendVerifyCode,
    val getCountries: GetCountries,
    val observeCountry: ObserveCountry,
    val selectedCurrentCountry: SelectedCurrentCountry,
    val register: Register,
    val touristRegisterAndLogin: TouristRegisterAndLogin,
    val updateNickName: UpdateNickName,
    val uploadUserAvatar: UploadUserAvatar,
    val getTuyaUserByUpdate: GetTuyaUserByUpdate,
    val getTuyaUser: GetTuyaUser,
)
