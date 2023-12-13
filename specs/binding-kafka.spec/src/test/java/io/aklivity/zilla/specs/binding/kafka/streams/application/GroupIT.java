/*
 * Copyright 2021-2023 Aklivity Inc.
 *
 * Aklivity licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.aklivity.zilla.specs.binding.kafka.streams.application;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class GroupIT
{
    private final K3poRule k3po = new K3poRule()
        .addScriptRoot("app", "io/aklivity/zilla/specs/binding/kafka/streams/application/group");

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @Specification({
        "${app}/rebalance.protocol.highlander/client",
        "${app}/rebalance.protocol.highlander/server"})
    public void shouldLeaveGroupOnGroupRebalanceError() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/client.sent.write.abort.before.coordinator.response/client",
        "${app}/client.sent.write.abort.before.coordinator.response/server"})
    public void shouldHandleClientSentWriteAbortBeforeCoordinatorResponse() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/client.sent.write.abort.after.sync.group.response/client",
        "${app}/client.sent.write.abort.after.sync.group.response/server"})
    public void shouldHandleClientSentWriteAbortAfterSyncGroupResponse() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/client.sent.read.abort.after.sync.group.response/client",
        "${app}/client.sent.read.abort.after.sync.group.response/server"})
    public void shouldHandleClientSentReadAbortAfterSyncGroupResponse() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/leader.assignment/client",
        "${app}/leader.assignment/server"})
    public void shouldAssignLeader() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.protocol.highlander.migrate.leader/client",
        "${app}/rebalance.protocol.highlander.migrate.leader/server"})
    public void shouldRebalanceProtocolHighlanderMigrateLeader() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.protocol.highlander.migrate.leader.in.parallel/client",
        "${app}/rebalance.protocol.highlander.migrate.leader.in.parallel/server"})
    public void shouldRebalanceProtocolHighlanderMigrateLeaderInParallel() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.protocol.unknown/client",
        "${app}/rebalance.protocol.unknown/server"})
    public void shouldRejectSecondStreamOnUnknownProtocol() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/ignore.heartbeat.before.handshake/client",
        "${app}/ignore.heartbeat.before.handshake/server"})
    public void shouldIgnoreHeartbeatBeforeHandshakeComplete() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.sync.group/client",
        "${app}/rebalance.sync.group/server"})
    public void shouldHandleRebalanceSyncGroup() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/partition.assignment/client",
        "${app}/partition.assignment/server"})
    public void shouldAssignGroupPartition() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/topics.partition.assignment/client",
        "${app}/topics.partition.assignment/server"})
    public void shouldAssignMultipleTopicsPartitionGroup() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/reassign.new.topic/client",
        "${app}/reassign.new.topic/server"})
    public void shouldReassignOnNewTopic() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.multiple.members.with.same.group.id/client",
        "${app}/rebalance.multiple.members.with.same.group.id/server"})
    public void shouldRebalanceMultipleMembersWithSameGroupId() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/rebalance.protocol.highlander.heartbeat.unknown.member/client",
        "${app}/rebalance.protocol.highlander.heartbeat.unknown.member/server"})
    public void shouldRebalanceProtocolHighlanderOnHeartbeatUnknownMember() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/group.authorization.failed/client",
        "${app}/group.authorization.failed/server"})
    public void shouldPropagateGroupAuthorizationFailedError() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/invalid.describe.config/client",
        "${app}/invalid.describe.config/server"})
    public void shouldHandleInvalidDescribeConfig() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${app}/invalid.session.timeout/client",
        "${app}/invalid.session.timeout/server"})
    public void shouldHandleInvalidSessionTimeout() throws Exception
    {
        k3po.finish();
    }
}
