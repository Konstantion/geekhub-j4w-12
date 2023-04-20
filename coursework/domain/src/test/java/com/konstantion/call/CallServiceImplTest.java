package com.konstantion.call;

import com.konstantion.call.model.CreateCallRequest;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CallServiceImplTest {
    @Mock
    CallPort callPort;
    @Mock
    TablePort tablePort;
    @Mock
    User user;
    UUID callId;
    UUID tableId;
    CreateCallRequest request;

    @InjectMocks
    CallServiceImpl callService;

    @BeforeEach
    void setUp() {
        callId = UUID.randomUUID();
        tableId = UUID.randomUUID();

        request = new CreateCallRequest(Purpose.CALL_BILL, tableId);
    }

    @Test
    void shouldReturnAllCallsWhenGetAll() {
        when(callPort.findAll()).thenReturn(
                List.of(
                        Call.builder().build(),
                        Call.builder().build())
        );

        List<Call> actualCalls = callService.getAll();

        assertThat(actualCalls).hasSize(2);
    }

    @Test
    void shouldReturnUserCallsWhenGetAllByUser() {
        UUID userId = UUID.randomUUID();

        when(callPort.findAll()).thenReturn(
                List.of(
                        Call.builder().waitersId(Set.of(userId)).build(),
                        Call.builder().build()
                ));
        when(user.getId()).thenReturn(userId);

        List<Call> actualCalls = callService.getAllByUser(user);

        assertThat(actualCalls).hasSize(1);
    }
}