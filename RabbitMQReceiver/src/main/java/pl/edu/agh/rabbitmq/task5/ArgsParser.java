package pl.edu.agh.rabbitmq.task5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ArgsParser {

    private final String queueNamePrefix;

    private ArgsParser(String queueNamePrefix) {
        this.queueNamePrefix = queueNamePrefix;
    }

    public static ArgsParser with(String queueNamePrefix) {
        return new ArgsParser(queueNamePrefix);
    }

    String composeQueueNameFrom(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("At least one argument should be provided.");
        }

        final List<String> argsList = Arrays.stream(args).map(this::replaceRabbitRegexCharactersFrom).collect(Collectors.toList());
        return composeQueueNameFrom(argsList);
    }

    private String composeQueueNameFrom(List<String> preparedArgs) {
        final int size = preparedArgs.size();
        if (size > 1) {
            String currentArgument = preparedArgs.remove(size - 1);
            return composeQueueNameFrom(preparedArgs) + "OR" + replaceRabbitRegexCharactersFrom(currentArgument);
        } else {
            return queueNamePrefix + replaceRabbitRegexCharactersFrom(preparedArgs.get(0));
        }
    }

    private String replaceRabbitRegexCharactersFrom(String s) {
        return s.replaceAll("#", "__").replaceAll("\\*", "_");
    }
}
