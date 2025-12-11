/*
 * This file is part of Cloth Config.
 * Copyright (C) 2020 - 2021 shedaniel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.clothconfigcoremods;

import net.neoforged.neoforgespi.transformation.ClassProcessor;
import net.neoforged.neoforgespi.transformation.ProcessorName;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClothConfigClassProcessor implements ClassProcessor {
    private static final ProcessorName NAME = new ProcessorName("cloth_config", "processor");
    
    @Override
    public ProcessorName name() {
        return NAME;
    }
    
    @Override
    public boolean handlesClass(SelectionContext context) {
        return context.type().getInternalName().equals("me/shedaniel/autoconfig/AutoConfig");
    }
    
    @Override
    public ComputeFlags processClass(TransformationContext context) {
        System.out.println("ClothConfigClassProcessor rewriting me.shedaniel.autoconfig.AutoConfig in order to preserve compatibility to pre-1.21.11 Cloth Config versions");
        
        MethodNode getGuiRegistry = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "getGuiRegistry", "(Ljava/lang/Class;)Lme/shedaniel/autoconfig/gui/registry/GuiRegistry;", null, null);
        getGuiRegistry.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getGuiRegistry.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/shedaniel/autoconfig/AutoConfigClient", "getGuiRegistry", "(Ljava/lang/Class;)Lme/shedaniel/autoconfig/gui/registry/GuiRegistry;", false));
        getGuiRegistry.instructions.add(new InsnNode(Opcodes.ARETURN));
        context.node().methods.add(getGuiRegistry);
        
        MethodNode getConfigScreen = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "getConfigScreen", "(Ljava/lang/Class;Lnet/minecraft/client/gui/screens/Screen;)Ljava/util/function/Supplier;", null, null);
        getConfigScreen.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getConfigScreen.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        getConfigScreen.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/shedaniel/autoconfig/AutoConfigClient", "getConfigScreen", "(Ljava/lang/Class;Lnet/minecraft/client/gui/screens/Screen;)Ljava/util/function/Supplier;", false));
        getConfigScreen.instructions.add(new InsnNode(Opcodes.ARETURN));
        context.node().methods.add(getConfigScreen);
        
        return ComputeFlags.COMPUTE_FRAMES;
    }
}
