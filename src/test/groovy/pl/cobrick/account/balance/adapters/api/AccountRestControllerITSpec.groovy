package pl.cobrick.account.balance.adapters.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.cobrick.account.balance.IntegrationSpecBase
import pl.cobrick.account.balance.domain.model.Account
import pl.cobrick.account.balance.domain.model.Balance
import pl.cobrick.account.balance.domain.model.Currency
import pl.cobrick.account.balance.domain.ports.ExchangeRateClient
import spock.lang.Shared

class AccountRestControllerITSpec extends IntegrationSpecBase {

    @MockitoBean
    ExchangeRateClient exchangeRateClient

    @Shared
    def objectMapper = new ObjectMapper()

    def "Should return OK when POST /accounts and create account"() {
        given:
        def request = new CreateAccountRequest(
                'Jan',
                'Testowy',
                '200.00',
                '100.00'
        )

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post('/accounts')
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "Should return OK when PATCH /accounts, transfer account balance from USD to PLN"() {
        given:
        Mockito.when(exchangeRateClient.getForCurrency(Currency.USD)).thenReturn(new BigDecimal('3.70'))

        and:
        def id = UUID.randomUUID().toString()
        def account = account(id)
        mongoTemplate.save(account)

        and:
        def request = new TransferBalanceRequest(
                'USD',
                'PLN',
                '100.00'
        )

        expect:
        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "Should return OK when PATCH /accounts, transfer account balance from PLN to USD "() {
        given:
        Mockito.when(exchangeRateClient.getForCurrency(Currency.USD)).thenReturn(new BigDecimal('3.70'))

        and:
        def id = UUID.randomUUID().toString()
        def account = account(id)
        mongoTemplate.save(account)

        and:
        def request = new TransferBalanceRequest(
                'PLN',
                'USD',
                '100.00'
        )

        expect:
        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "Should return BAD REQUEST when PATCH /accounts, transfer account balance from PLN to USD and amount is greater than balance"() {
        given:
        Mockito.when(exchangeRateClient.getForCurrency(Currency.USD)).thenReturn(new BigDecimal('3.70'))

        and:
        def id = UUID.randomUUID().toString()
        def account = account(id)
        mongoTemplate.save(account)

        and:
        def request = new TransferBalanceRequest(
                'PLN',
                'USD',
                '10000.00'
        )

        expect:
        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "Should return BAD REQUEST when PATCH /accounts, transfer account balance from USD to PLN and amount is greater than balance"() {
        given:
        Mockito.when(exchangeRateClient.getForCurrency(Currency.USD)).thenReturn(new BigDecimal('3.70'))

        and:
        def id = UUID.randomUUID().toString()
        def account = account(id)
        mongoTemplate.save(account)

        and:
        def request = new TransferBalanceRequest(
                'USD',
                'PLN',
                '10000.00'
        )

        expect:
        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "Should return OK when GET /accounts and find account by given id"() {
        given:
        def id = UUID.randomUUID().toString()
        def account = account(id)
        mongoTemplate.save(account)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/${id}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id').value(id))
    }

    def account(String id) {
        new Account(
                id,
                null,
                'Jan',
                'Kowalski',
                new Balance(Currency.USD, new BigDecimal('200.00')),
                new Balance(Currency.PLN, new BigDecimal('200.00'))
        )
    }
}
