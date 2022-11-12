package com.jungma.nare.gun.listener

import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent

class GunSystemListener : Listener {
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (!e.hasItem() || e.item?.type != Material.DIAMOND_SWORD || e.action.isLeftClick) return

        val player = e.player
        player.launchProjectile(Arrow::class.java).apply {
            velocity = player.location.direction.multiply(10)
        }
    }

    @EventHandler
    fun onBullet(e: ProjectileHitEvent) {
        val bullet = e.entity as? Arrow ?: return
        bullet.apply {
            remove()
            damage = 0.0
        }
    }

    @EventHandler
    fun onBulletHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return

        // GunManager - get gun damage...?
        val livingEntity = e.entity as? LivingEntity ?: return
        livingEntity.apply {
            damage(1.0)
        }
    }

    @EventHandler
    fun onBulletNoDamageTicks(e: EntityDamageByEntityEvent) {
        val livingEntity = e.entity as? LivingEntity ?: return
        livingEntity.apply {
            maximumNoDamageTicks = if (e.damager is Arrow) 0 else 20
        }
    }
}
