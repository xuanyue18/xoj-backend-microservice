package com.xuanyue.xojcodesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.util.List;

/**
 * 连接Docker案例
 */
public class DockerDemo {
    private DockerClient dockerClient;

    public DockerDemo() {
        // 获取默认的Docker Client
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }

    public static void main(String[] args) throws InterruptedException {
        DockerDemo demo = new DockerDemo();
        demo.run();
    }

    public void run() throws InterruptedException {
        pingDockerDaemon();
        String image = "nginx:latest";
        pullImage(image);
        String containerId = createAndStartContainer(image);
        listContainers();
        logContainer(containerId);
        removeContainer(containerId);
    }

    private void pingDockerDaemon() {
        PingCmd pingCmd = dockerClient.pingCmd();
        pingCmd.exec();
    }

    private void pullImage(String image) throws InterruptedException {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像: " + item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
        System.out.println("下载完成");
    }

    private String createAndStartContainer(String image) {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse createContainerResponse = containerCmd
                .withCmd("echo", "Hello Docker")
                .exec();
        String containerId = createContainerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();
        return containerId;
    }

    private void listContainers() {
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            System.out.println(container);
        }
    }

    private void logContainer(String containerId) throws InterruptedException {
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println(item.getStreamType());
                System.out.println("日志：" + new String(item.getPayload()));
                super.onNext(item);
            }
        };
        dockerClient.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();
    }

    private void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
    }
}