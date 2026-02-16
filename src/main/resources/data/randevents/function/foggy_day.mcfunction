title @s actionbar "Сьогодні туманно..."
playsound minecraft:entity.breeze.whirl master @s ~ ~ ~ 2 0.7

summon area_effect_cloud ~ ~ ~ {Particle:{type:angry_villager},Radius:10,RadiusPerTick:-0.01,Duration:500,potion_contents:{custom_color:11972274,custom_effects:[{id:slow_falling,duration:25,amplifier:3,show_particles:0b,show_icon:0}]}}

summon area_effect_cloud ~ ~0.7 ~ {Particle:{type:angry_villager},Radius:7,RadiusPerTick:-0.02,Duration:500,potion_contents:{custom_color:11972274,custom_effects:[{id:slow_falling,duration:25,amplifier:3,show_particles:0b,show_icon:0}]}}

summon area_effect_cloud ~ ~1.4 ~ {Particle:{type:angry_villager},Radius:5,RadiusPerTick:-0.02,Duration:500,potion_contents:{custom_color:11972274,custom_effects:[{id:slow_falling,duration:25,amplifier:3,show_particles:0b,show_icon:0}]}}
