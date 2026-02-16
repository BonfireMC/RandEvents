package xyz.bonfiremc.randevents.impl.actions;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

import java.util.List;

@ApiStatus.Internal
public record RandomPhraseREAction() implements REAction {
    public static final List<String> PHRASES = List.of(
            "Один хлопчик не вмів вимовляти слово шість, заходить в магазин та й каже: дайте сім пачок масла, одну не треба",
            "Один хлопчик не вмів вимовляти слово шість, заходить в магазин та й каже: дайте вісім пачок масла, дві не треба",
            "Один казах не вмів вимовляти слово курка, підходить він якось з яйцями до консультанта і такий: дє мать?",
            "Я гей",
            "фелес лох",
            "ГОЛОСУЙ ЗА ПОРОШЕНКА, БО БЛЯТЬ путін НАПАДЕ!!!!!",
            "o kurwa",
            "рабой лох",
            "ХТО ТРИМАЄ ЦЕЙ РАЙОН? ПЕС ПАТРОН, ПЕС ПАТРОН",
            "христос воскрес",
            "з новим роком",
            "бу",
            "вайдер, якшо ти це читаєш, то ти маєш доєднатися до соковитого",
            "ПАТУЖНАААА",
            "хз що сюди писати, але я зараз неймовірно сильно хочу схавати піцу",
            "хто не скаче той москаль",
            "іді нахуй",
            "немає сечі терпіти ці пекельні борошна",
            "підпіськуйтесь на потомкіча!!1!11! ну і на інших теж можете"
    );

    @Override
    public REActionType type() {
        return REActions.RANDOM_PHRASE;
    }

    @Override
    public void execute(REContext context) {
        String phrase = PHRASES.get(context.player().level().random.nextInt(PHRASES.size()));

        context.player().level().getServer().getPlayerList().broadcastSystemMessage(Component.literal(phrase), false);
    }
}
