/*
 * Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.tcp;

import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * {@link TcpFactory} creates {@link TcpClient}.
 */
public class TcpFactory {

    private static volatile TcpFactory tcpFactory;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private TcpFactory() {
        int totalNumberOfProcessors = Runtime.getRuntime().availableProcessors();
        bossGroup = new NioEventLoopGroup(totalNumberOfProcessors);
        workerGroup = new NioEventLoopGroup(totalNumberOfProcessors * 2);
    }

    public static TcpFactory getInstance() {
        if (tcpFactory == null) {
            tcpFactory = new TcpFactory();
        }
        return tcpFactory;
    }

    public TcpClient createTcpClient(InetSocketAddress localAddress, InetSocketAddress remoteAddress,
                                     CompletableFuture<Object> callback, BMap<BString, Object> secureSocket) {
        return new TcpClient(localAddress, remoteAddress, workerGroup, callback, secureSocket);
    }

    public TcpListener createTcpListener(InetSocketAddress localAddress,
                                         CompletableFuture<Object> callback, TcpService tcpService,
                                         BMap<BString, Object> secureSocket) {
        return new TcpListener(localAddress, bossGroup, workerGroup, callback, tcpService,
                secureSocket);
    }
}
