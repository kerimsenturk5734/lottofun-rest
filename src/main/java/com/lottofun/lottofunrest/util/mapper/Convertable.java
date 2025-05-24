package com.lottofun.lottofunrest.util.mapper;

public interface Convertable<Input, Output> {
    public Output convert(Input input);
    public Input deConvert(Output output);
}
