package fr.geming400.pesticide.content;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.Pesticides;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

@SuppressWarnings("UnstableApiUsage")
public final class ModAttachments {
    public static final AttachmentType<Integer> TIME_SPENT_ON_INFESTED_FARMLAND = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "time_spent_on_infested_farmland"),
            builder -> builder
                    .persistent(Codec.INT)
                    .initializer(() -> 0)
    );

    public static void initialize() {}

    private ModAttachments() {}
}
