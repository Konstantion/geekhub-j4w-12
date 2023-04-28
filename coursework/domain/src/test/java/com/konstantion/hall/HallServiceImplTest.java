package com.konstantion.hall;

import com.github.javafaker.Faker;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.exception.ValidationException;
import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.hall.model.UpdateHallRequest;
import com.konstantion.hall.validator.HallValidator;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.Permission;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class HallServiceImplTest {
    @Mock
    HallPort hallPort;
    @Mock
    TablePort tablePort;
    @Mock
    HallValidator hallValidator;
    @InjectMocks
    HallServiceImpl hallService;
    @Mock
    User user;
    Faker faker;

    @BeforeEach
    void setUp() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        faker = new Faker();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenCallMethodWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> hallService.create(null, user)).isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> hallService.deactivate(null, user)).isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> hallService.delete(null, user)).isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> hallService.activate(null, user)).isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> hallService.update(null, null, user)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetByIdWithNonExistingId() {
        when(hallPort.findById(any())).thenReturn(Optional.empty());
        UUID randomUUID = UUID.randomUUID();

        assertThatThrownBy(() -> hallService.getById(randomUUID))
                .isExactlyInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldReturnHallWhenGetByIdWithExistingId() {
        when(hallPort.findById(any())).thenReturn(Optional.of(Hall.builder().build()));

        Hall actual = hallService.getById(UUID.randomUUID());

        assertThat(actual).isNotNull();
    }

    @Test
    void shouldReturnHallWhenGetAll() {
        when(hallPort.findAll()).thenReturn(List.of(
                Hall.builder().active(true).build(),
                Hall.builder().active(false).build()
        ));

        List<Hall> actualActive = hallService.getAll(true);
        List<Hall> actualAll = hallService.getAll(false);

        assertThat(actualActive)
                .hasSize(1)
                .containsExactlyInAnyOrder(Hall.builder().active(true).build());
        assertThat(actualAll).hasSize(2);
    }

    @Test
    void shouldDeleteHallWhenDelete() {
        Hall hall = Hall.builder().build();
        when(hallPort.findById(any())).thenReturn(Optional.of(hall));

        Hall actualDeleted = hallService.delete(UUID.randomUUID(), user);

        assertThat(actualDeleted)
                .isNotNull()
                .isEqualTo(hall);
        verify(hallPort, times(1)).delete(hall);
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateWithInvalidData() {
        CreateHallRequest request = new CreateHallRequest(faker.name().firstName());
        when(hallValidator.validate(any(CreateHallRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));

        assertThatThrownBy(() -> hallService.create(request, user))
                .isExactlyInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateWithInvalidData() {
        UpdateHallRequest request = new UpdateHallRequest(faker.name().firstName());
        when(hallValidator.validate(any(UpdateHallRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));
        UUID randomUUID = UUID.randomUUID();

        assertThatThrownBy(() -> hallService.update(randomUUID, request, user))
                .isExactlyInstanceOf(ValidationException.class);
    }

    @Test
    void shouldCreateHallWhenCreateWithValidData() {
        String name = faker.name().firstName();
        CreateHallRequest request = new CreateHallRequest(name);
        when(hallValidator.validate(any(CreateHallRequest.class))).thenReturn(ValidationResult.valid());

        Hall actualCreated = hallService.create(request, user);

        assertThat(actualCreated)
                .isNotNull()
                .extracting(Hall::getName)
                .isEqualTo(name);
        verify(hallPort, times(1)).save(any());
    }

    @Test
    void shouldUpdateHallWhenUpdateWithValidData() {
        String name = faker.name().firstName();
        UpdateHallRequest request = new UpdateHallRequest(name);
        when(hallValidator.validate(any(UpdateHallRequest.class))).thenReturn(ValidationResult.valid());
        when(hallPort.findById(any())).thenReturn(Optional.of(Hall.builder().build()));

        Hall actualUpdated = hallService.update(UUID.randomUUID(), request, user);

        assertThat(actualUpdated)
                .isNotNull()
                .extracting(Hall::getName)
                .isEqualTo(name);
        verify(hallPort, times(1)).save(any());
    }

    @Test
    void shouldActivateWhenActivate() {
        when(hallPort.findById(any()))
                .thenReturn(Optional.of(Hall.builder().active(true).build()))
                .thenReturn(Optional.of(Hall.builder().active(false).build()));

        Hall activeHall = hallService.activate(UUID.randomUUID(), user);
        Hall activetedHall = hallService.activate(UUID.randomUUID(), user);

        assertThat(activeHall).isNotNull().extracting(Hall::getActive).isEqualTo(true);
        assertThat(activetedHall).isNotNull().extracting(Hall::getActive).isEqualTo(true);

        verify(hallPort, times(1)).save(activetedHall);
    }

    @Test
    void shouldDeactivateWhenActivate() {
        when(hallPort.findById(any()))
                .thenReturn(Optional.of(Hall.builder().active(true).build()))
                .thenReturn(Optional.of(Hall.builder().active(false).build()));

        Hall deactivatedHall = hallService.deactivate(UUID.randomUUID(), user);
        Hall inactiveHall = hallService.deactivate(UUID.randomUUID(), user);

        assertThat(deactivatedHall).isNotNull().extracting(Hall::getActive).isEqualTo(false);
        assertThat(inactiveHall).isNotNull().extracting(Hall::getActive).isEqualTo(false);

        verify(hallPort, times(1)).save(deactivatedHall);
    }

    @Test
    void shouldReturnTablesWhenGetTablesByHallId() {
        when(hallPort.findById(any())).thenReturn(Optional.of(Hall.builder().id(UUID.randomUUID()).build()));
        when(tablePort.findAllWhereHallId(any())).thenReturn(List.of(
                Table.builder().build(),
                Table.builder().build()
        ));

        List<Table> tables = hallService.getTablesByHallId(UUID.randomUUID());

        assertThat(tables)
                .hasSize(2);
    }
}