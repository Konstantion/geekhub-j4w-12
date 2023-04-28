package com.konstantion.api.web.controllers.bill;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konstantion.api.configuration.MockedBeansConfiguration;
import com.konstantion.bill.Bill;
import com.konstantion.bill.BillService;
import com.konstantion.controllers.bill.BillController;
import com.konstantion.dto.bill.dto.CreateBillRequestDto;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.jwt.JwtService;
import com.konstantion.response.ResponseDto;
import com.konstantion.security.configuration.AuthenticationManagerConfig;
import com.konstantion.security.configuration.PasswordEncoderConfig;
import com.konstantion.security.configuration.SecurityConfig;
import com.konstantion.security.jwt.JwtAuthorizationFilter;
import com.konstantion.security.jwt.config.JwtConfig;
import com.konstantion.security.table.TableAuthenticationProvider;
import com.konstantion.security.user.UserAuthenticationProvider;
import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.authentication.UserAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static com.konstantion.api.configuration.UserDetailsFactory.waiter;
import static com.konstantion.utils.EntityNameConstants.BILL;
import static com.konstantion.utils.EntityNameConstants.BILLS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BillController.class, useDefaultFilters = false)
@ContextConfiguration(classes = {
        AuthenticationManagerConfig.class,
        UserAuthenticationProvider.class,
        TableAuthenticationProvider.class,
        PasswordEncoderConfig.class,
        MockedBeansConfiguration.class,
        JwtAuthorizationFilter.class,
        JwtConfig.class
})
@Import(SecurityConfig.class)
class BillControllerTest {
    @Autowired
    MockedBeansConfiguration mocks;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BillService billService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test")
                .id(UUID.randomUUID())
                .active(true)
                .password("test")
                .roles(Role.getWaiterRoles())
                .permissions(Permission.getDefaultWaiterPermission())
                .build();
    }

    @Test
    void shouldReturnUnauthorizedWhenGetAllActiveBillsWithUnauthorizedUser() throws Exception {

        mockMvc.perform(get("/web-api/bills"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "WAITER")
    void shouldReturnBillsWhenGetAllActiveBillsWithAuthorizedUser() throws Exception {
        UUID billId = UUID.randomUUID();
        List<Bill> bills = List.of(Bill.builder().id(billId).build());
        when(billService.getAll()).thenReturn(List.of(Bill.builder().id(billId).build()));
        when(mocks.jwtService().extractUsername(anyString())).thenReturn("waiter");
        when(mocks.jwtService().extractClaim(any(), any())).thenReturn("user");
        when(mocks.jwtService().isTokenValid(any(), any())).thenReturn(true);
        when(mocks.userService().loadUserByUsername(any())).thenReturn(waiter());

        MvcResult mvcResult = mockMvc.perform(get("/web-api/bills")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        ResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        List<Bill> responseBills = objectMapper.convertValue(
                responseDto.data().get(BILLS),
                new TypeReference<>() {
                }
        );

        assertThat(responseBills).isEqualTo(bills);

        verify(billService, times(1)).getAll();
    }

    @Test
    @WithUserDetails("waiter")
    void shouldReturnBillByIdWhenGetBillById() throws Exception {
        UUID billId = UUID.randomUUID();
        when(billService.getById(billId)).thenReturn(Bill.builder().id(billId).build());

        MvcResult mvcResult = mockMvc.perform(get("/web-api/bills/{id}", billId))
                .andExpect(status().isOk())
                .andReturn();

        ResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        Bill responseBill = objectMapper.convertValue(
                responseDto.data().get(BILL),
                Bill.class);

        assertThat(responseBill.getId()).isEqualTo(billId);

        verify(billService, times(1)).getById(billId);
    }

    @Test
    @WithMockUser(roles = "WAITER")
    void shouldReturnBadRequestWhenGetBillByIdWithNonExistingId() throws Exception {
        when(billService.getById(any())).thenThrow(new NonExistingIdException("test"));

        mockMvc.perform(get("/web-api/bills/{id}", UUID.randomUUID()))
                .andExpect(status().isBadRequest());

        verify(billService, times(1)).getById(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBillWhenCreateBill() throws Exception {
        CreateBillRequestDto requestDto = new CreateBillRequestDto(null, null);
        String json = objectMapper.writeValueAsString(requestDto);

        UUID billId = UUID.randomUUID();
        when(billService.create(any(), any())).thenReturn(Bill.builder().id(billId).build());

        UsernamePasswordAuthenticationToken authenticationToken = new UserAuthenticationToken(user, user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        MvcResult mvcResult = mockMvc.perform(post("/web-api/bills")
                        .accept(APPLICATION_JSON)
                        .content(json).contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        Bill responseBill = objectMapper.convertValue(responseDto.data().get(BILL), Bill.class);

        assertThat(responseBill.getId()).isEqualTo(billId);
    }
}