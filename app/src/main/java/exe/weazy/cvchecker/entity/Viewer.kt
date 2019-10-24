package exe.weazy.cvchecker.entity

data class Viewer(
    val name : String,
    val surname : String,
    val patronymic : String,
    val uid : String,
    val rank: Rank,
    val email : String
)