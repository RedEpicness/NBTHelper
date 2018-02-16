package me.redepicness.nbthelper.util;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagString;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Red_Epicness on 6/10/2016 at 9:45 PM.
 */
public class StackManager {

    private ItemStack stack;
    private ItemMeta meta;
    private NBTTagCompound tag;

    public StackManager(Material type) {
        this(new ItemStack(type));
    }

    public StackManager(Material type, int amount) {
        this(new ItemStack(type, amount));
    }

    public StackManager(Material type, int amount, short damage) {
        this(new ItemStack(type, amount, damage));
    }

    public StackManager(ItemStack s){
        stack = s;
        meta = s.getItemMeta();
        tag = getTag();
    }

    public void setData(int data){
        Validate.isTrue(data < 32767 && data > -32768 , "Data must be between -32768 and 32767!");
        stack.setDurability((short)data);
    }

    public void setCount(int count){
        Validate.isTrue(count < 128 && count > -129, "Count must be between -129 and 128!");
        stack.setAmount(count);
    }

    public void setCanDestroy(String... canDestroy){
        setCanDestroy(Arrays.asList(canDestroy));
    }

    public void setCanDestroy(List<String> canDestroy){
        NBTTagList list = new NBTTagList();
        for(String s : canDestroy){
            list.add(new NBTTagString(s));
        }
        tag.set("CanDestroy", list);
    }

    public ArrayList<String> getCanDestroy(){
        NBTTagList list = tag.getList("CanDestroy", 8);
        ArrayList<String> canDestroy = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            canDestroy.add(list.getString(i));
        }
        return canDestroy;
    }

    public void addHideFlags(ItemFlag... flags){
        meta.addItemFlags(flags);
    }

    public void removeHideFlags(ItemFlag... flags){
        meta.removeItemFlags(flags);
    }

    public void clearHideFlags(){
        meta.removeItemFlags(ItemFlag.values());
    }

    public void setName(String name){
        meta.setDisplayName(name);
    }

    public void setLore(String... lore){
        setLore(Arrays.asList(lore));
    }

    public void setLore(List<String> lore){
        meta.setLore(lore);
    }

    public List<String> getLore(){
        return meta.getLore()==null?new ArrayList<>():meta.getLore();
    }

    public void setUnbreakable(boolean unbreakable){
        tag.setBoolean("Unbreakable", unbreakable);
    }

    public void addEnchantment(Enchantment e, int level){
        meta.addEnchant(e, level, true);
    }

    public void removeEnchantment(Enchantment e){
        meta.removeEnchant(e);
    }

    public void clearEnchantments(){
        for(Enchantment e : Enchantment.values()){
            meta.removeEnchant(e);
        }
    }

    public ItemStack get(){
        net.minecraft.server.v1_10_R1.ItemStack s = CraftItemStack.asNMSCopy(stack);
        s.setTag(tag);
        stack = CraftItemStack.asCraftMirror(s);
        updateMeta();
        return stack;
    }

    private void updateMeta(){
        stack.setItemMeta(meta);
    }

    private NBTTagCompound getTag(){
        net.minecraft.server.v1_10_R1.ItemStack s = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag;
        if (!s.hasTag()) {
            tag = new NBTTagCompound();
            s.setTag(tag);
        }
        else {
            tag = s.getTag();
        }
        return tag;
    }

}
