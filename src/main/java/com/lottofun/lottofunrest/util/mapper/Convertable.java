package com.lottofun.lottofunrest.util.mapper;

public interface Convertable<Input, Output> {
    Output convert(Input input);
    Input deConvert(Output output);
}
