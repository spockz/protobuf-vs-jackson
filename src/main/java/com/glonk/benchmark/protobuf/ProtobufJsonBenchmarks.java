package com.glonk.benchmark.protobuf;

import com.glonk.benchmark.Benchmark;
import com.glonk.benchmark.BenchmarkSet;
import com.glonk.benchmark.protobuf.model.Obj;
import com.glonk.benchmark.protobuf.model.Type;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessandro on 17/09/16.
 */
public class ProtobufJsonBenchmarks implements BenchmarkSet {
    private static final String name = "protobuf-json";

    private final int numChildren;
    final JsonFormat.Parser jsonParser = JsonFormat.parser();
    final JsonFormat.Printer jsonPrinter = JsonFormat.printer();

    public ProtobufJsonBenchmarks(int numChildren) {
        this.numChildren = numChildren;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Benchmark serialization() {
        final Obj obj = buildObject("parent");
        return new Benchmark() {
            @Override
            public String name() {
                return name + " serialization";
            }
            @Override
            public void run() throws Exception {
                jsonPrinter.print(obj);
            }
        };
    }

    @Override
    public Benchmark deserialization() throws InvalidProtocolBufferException {
        Obj obj = buildObject("parent");

        final String jsonString = jsonPrinter.print(obj);
        return new Benchmark() {
            @Override
            public String name() {
                return name + " deserialization";
            }
            @Override
            public void run() throws Exception {
                jsonParser.merge(jsonString, Obj.newBuilder());
            }
        };
    }

    @Override
    public int encodedSize() throws InvalidProtocolBufferException {
        Obj obj = buildObject("parent");
        return jsonPrinter.print(obj).length();
    }

    private Obj buildObject(String name) {
        return buildObject(name, true);
    }

    private Obj buildObject(String name, boolean children) {
        Obj.Builder builder = Obj.newBuilder()
                .setType(Type.FOO)
                .setNum32(Integer.MAX_VALUE)
                .setNum64(Long.MAX_VALUE)
                .setStr(name);
        if (children) {
            for (int i = 0; i < numChildren; i++) {
                builder.addChildren(buildObject("child " + (i + 1), false));
            }
        }
        return builder.build();
    }
}
