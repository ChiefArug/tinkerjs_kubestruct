# TinkerJS Kubestruct
Addon integrating parts of Tinkers Construct with KubeJS

This is currently in early alpha, bugs are to be expected. I have done quite a bit of testing but you never know what crazy things people will make. 

Full wiki coming soon!

Examples:
```js
onEvent('item.registry', event => {
	event.create('shovel', 'tconstruct:modifiable')
	    .part(Part.handle.setTexture('tool_rod_top'))
	    .part(Part.handle.setTexture('tool_rod_bottom'))
        .part(Part.of('round_plate')
            .setTexture('head')
            .setBrokenTexture('broken_head')
        )
        .pickaxeLayout()
        .effectiveTagHarvest('minecraft:mineable/shovel')
        .veinAoe(0)
        .statMultiplier('durability', 0.9)
        .statMultiplier('BASE', 'mining_speed', 0.7)
        .statMultiplier('attack_damage', 1.2)
        .addModifierTextureLocation('tconstruct:item/tool/pickaxe/modifiers/')
        .description("Popsicles!")
        .action('shovel_dig')
        .action('shovel_flatten')
        .trait('shovel_flatten')

    event.create('frisbee', 'tconstruct:modifiable')
        .part(Part.roundPlate.setTexture('tconstruct:item/tool/parts/round_plate')) // You can use any existing part/tool part texture, or make your own
        .circleAttack(10) // This is just stupid op
        // If you leave out the type for stat multipliers it defaults to 'modifier', which is applied after modifiers
        // Base is applied before modifiers
        .statMultiplier('attack_speed', 0.1)
        .statMultiplier('base', 'attack_damage', 2.0) // Reduce efficiency of attack damage modifiers by quarter, and base by half
        .statMultiplier('modifier', 'attack_damage', 0.25)
        .statMultiplier('base', 'durability', 1.5)
        .statMultiplier('mining_speed', 0) // No mining for you!
        .description("Bounces between entities instantly to hurt large groups at once!")

    event.create('pickaxe', 'tconstruct:modifiable') // Perfect copy of the pickaxe, only exception is the model layers are in the wrong order.
        .description("Totally a pickaxe. It breaks blocks, OK?")
        .part(Part.handle)
        .part(Part.binding)
        .part(Part.pickHead)
        .pickaxeLayout()
        .addModifierTextureLocation('tconstruct:item/tool/pickaxe/modifiers/')
        .statMultiplier('base', 'attack_damage', 0.5)
        .statMultiplier('base', 'attack_speed' , 1.2)
        .trait('piercing')
        .effectiveTagHarvest('minecraft:mineable/pickaxe')
        .boxAoe('pitch', 1, 1, 0) // side_hit, pitch or height. See IBoxExpansion
        .action('pickaxe_dig')
        .attack({}) // This sets attack to true but prevents any json from being added cause its empty. .tag('tconstruct:modifiable/melee') would achieve the same effects.

    event.create('bow', 'tconstruct:modifiable')
        .description('Totally a bow. Throwable.')
        .part(Part.handle.setTexture('tconstruct:item/tool/bow/limb_bottom'))
        .part(Part.handle.setTexture('tconstruct:item/tool/bow/limb_top'))
        .part(Part.hammerHead.setTexture('tconstruct:item/tool/bow/bowstring').setBrokenTexture('tconstruct:item/tool/bow/bowstring_broken'))
        .particleAttack('crit')
        .statMultiplier('base', 'attack_damage', 1.5)
        .statMultiplier('modifier', 'attack_speed', 0.1)

    event.create('two_sticks', 'tconstruct:modifiable')
        .description('Rub em!')
        .part(Part.blade.setTexture('stick_1').setBrokenTexture('stick_1_broken'))
        .part(Part.blade.setTexture('stick_2').setBrokenTexture('stick_2_broken'))
        .trait('firestarter')
        .statMultiplier('attack_damage', 0.1)
        .statMultiplier('attack_speed', 0.1)
        .trait('fiery')
        .statMultiplier('base', 'durability', 2)
})
```
