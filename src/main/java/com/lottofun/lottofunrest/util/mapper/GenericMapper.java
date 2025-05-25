package com.lottofun.lottofunrest.util.mapper;

import java.util.List;
import java.util.function.Function;

/**
 * A generic bidirectional mapper that provides conversion logic between two types.
 * This class implements the {@link Convertable} interface and supports both forward (Input -> Output)
 * and reverse (Output -> Input) transformations.
 *
 * @param <Input>  the source type
 * @param <Output> the target type
 */
public class GenericMapper<Input, Output> implements Convertable<Input, Output> {

    private final Function<Input, Output> forwardMapper;
    private final Function<Output, Input> reverseMapper;

    /**
     * Constructs a GenericMapper with specified forward and reverse mapping functions.
     *
     * @param forwardMapper the function used to convert from Input to Output
     * @param reverseMapper the function used to convert from Output to Input
     */
    private GenericMapper(Function<Input, Output> forwardMapper, Function<Output, Input> reverseMapper) {
        this.forwardMapper = forwardMapper;
        this.reverseMapper = reverseMapper;
    }

    /**
     * Factory method to create a {@link GenericMapper} instance with the provided mapping functions.
     *
     * @param forward the forward mapping function (Input to Output)
     * @param reverse the reverse mapping function (Output to Input)
     * @param <I>     the input type
     * @param <O>     the output type
     * @return a new instance of {@code GenericMapper<I, O>}
     */
    public static <I, O> GenericMapper<I, O> createMap(Function<I, O> forward, Function<O, I> reverse) {
        return new GenericMapper<>(forward, reverse);
    }

    /**
     * Converts an object of type Input to type Output using the provided forward mapping function.
     *
     * @param input the input object to convert
     * @return the converted output object
     */
    @Override
    public Output convert(Input input) {
        return forwardMapper.apply(input);
    }

    /**
     * Converts an object of type Output back to type Input using the provided reverse mapping function.
     *
     * @param output the output object to convert back
     * @return the converted input object
     */
    @Override
    public Input deConvert(Output output) {
        return reverseMapper.apply(output);
    }

    /**
     * Converts a list of input objects to a list of output objects using the forward mapping function.
     *
     * @param inputs the list of input objects
     * @return the list of converted output objects
     */
    public List<Output> convertAll(List<Input> inputs) {
        return inputs.stream().map(this::convert).toList();
    }

    /**
     * Converts a list of output objects to a list of input objects using the reverse mapping function.
     *
     * @param outputs the list of output objects
     * @return the list of converted input objects
     */
    public List<Input> deConvertAll(List<Output> outputs) {
        return outputs.stream().map(this::deConvert).toList();
    }
}

