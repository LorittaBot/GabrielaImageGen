package net.perfectdreams.imageserver.routes

import net.perfectdreams.imageserver.utils.Constants

abstract class VersionedAPIRoute(path: String) : BaseRoute(
    "/api/${Constants.API_VERSION}$path"
)