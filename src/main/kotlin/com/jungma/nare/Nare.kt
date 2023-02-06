package com.jungma.nare

import com.jungma.nare.gun.listener.GunSystemListener
import org.bukkit.plugin.java.JavaPlugin

class Nare : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(GunSystemListener(), this)
    }
}
