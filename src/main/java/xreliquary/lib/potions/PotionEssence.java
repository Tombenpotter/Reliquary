package xreliquary.lib.potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * PotionEssence, the helper class for well, potion essences.
 * Abstracts away juggling all that NBT data, combining effects, and applying effects.
 *
 * @author TheMike
 */
public class PotionEssence {

    public static int MAX_DURATION = 4500000;
    public List<PotionEffect> potionEffects = new ArrayList<PotionEffect>();

    public PotionEssence(NBTTagCompound tag) {
        if(tag == null)
            return;

        for(Object object : tag.func_150296_c()) {
            if(!(object instanceof NBTTagCompound))
                continue;
            NBTTagCompound effect = (NBTTagCompound) object;
            potionEffects.add(new PotionEffect(effect.getInteger("id"), effect.getInteger("duration"), effect.getInteger("potency")));
        }
    }

    public PotionEssence(PotionEffect... effects) {
        for(PotionEffect effect : effects) {
            potionEffects.add(effect);
        }
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        int count = 0;
        for(PotionEffect object : potionEffects) {
            NBTTagCompound effect = new NBTTagCompound();
            effect.setInteger("id", object.getPotionID());
            effect.setInteger("duration", object.getDuration());
            effect.setInteger("potency", object.getAmplifier());
            tag.setTag("effect" + count, effect);
            count++;
        }
        return tag;
    }

    public void combine(PotionEssence essence) {
        for(PotionEffect effect : essence.potionEffects) {
            boolean found = false;
            for(PotionEffect effect1 : potionEffects) {
                if(effect1.getPotionID() != effect.getPotionID())
                    continue;

                found = true;
                PotionEffect newEffect = new PotionEffect(effect1.getPotionID(), effect1.getDuration() >= effect.getDuration() ? effect1.getDuration() : effect.getDuration(), effect1.getAmplifier() >= effect.getAmplifier() ? effect1.getAmplifier() : effect.getAmplifier());
                potionEffects.remove(effect1);
                potionEffects.add(newEffect);
            }
            if(!found)
                potionEffects.add(effect);
        }
    }

    public void apply(EntityPlayer player) {
        for(PotionEffect effect : potionEffects) {
            player.addPotionEffect(effect);
        }
    }

}
