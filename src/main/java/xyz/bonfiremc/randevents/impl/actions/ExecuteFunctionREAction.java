package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

@ApiStatus.Internal
public record ExecuteFunctionREAction(Identifier function, boolean forcePlayer, boolean isMacro) implements REAction {
    public static final MapCodec<ExecuteFunctionREAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("function").forGetter(ExecuteFunctionREAction::function),
            Codec.BOOL.optionalFieldOf("force_player", false).forGetter(ExecuteFunctionREAction::forcePlayer),
            Codec.BOOL.optionalFieldOf("is_macro", false).forGetter(ExecuteFunctionREAction::isMacro)
    ).apply(instance, ExecuteFunctionREAction::new));

    @Override
    public REActionType type() {
        return REActions.EXECUTE_FUNCTION;
    }

    @Override
    public void execute(REContext context) {
        MinecraftServer server = context.player().level().getServer();
        ServerFunctionManager manager = server.getFunctions();

        manager.get(this.function).ifPresent(func -> {
            CompoundTag tag;

            if (this.isMacro) {
                tag = new CompoundTag();
                context.write(tag);
            } else {
                tag = null;
            }

            CommandSourceStack source = this.forcePlayer
                    ? context.player().createCommandSourceStack().withSuppressedOutput()
                    : context.createCommandSourceStack();

            try {
                InstantiatedFunction<@NotNull CommandSourceStack> instantiatedFunction = func.instantiate(tag, manager.getDispatcher());
                Commands.executeCommandInContext(
                        source,
                        executionContext -> ExecutionContext.queueInitialFunctionCall(executionContext, instantiatedFunction, source, CommandResultCallback.EMPTY)
                );
            } catch (FunctionInstantiationException ignored) {
            }
        });
    }
}
