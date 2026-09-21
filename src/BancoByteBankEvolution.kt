import java.util.Locale

// ============================================================
// EXCEÇÕES PERSONALIZADAS
// Usadas para impedir movimentações inválidas (regra do banco)
// ============================================================
class ValorInvalidoException(mensagem: String) : Exception(mensagem)
class SaldoInsuficienteException(mensagem: String) : Exception(mensagem)

// ============================================================
// CLASSE ABSTRATA - Conta
// Concentra tudo o que é comum a QUALQUER conta do banco
// (abstração + encapsulamento). Cada subclasse só precisa
// implementar o que é realmente específico dela.
// ============================================================
abstract class Conta(
    val numero: Int,
    val titular: String,
    saldoInicial: Double = 0.0
) {
    // Encapsulamento: saldo só pode ser alterado por dentro da própria classe/subclasses
    var saldo: Double = saldoInicial
        protected set

    protected val historico = mutableListOf<String>()

    fun depositar(valor: Double) {
        if (valor <= 0.0) {
            throw ValorInvalidoException("Depósito inválido! O valor deve ser maior que zero.")
        }
        saldo += valor
        registrarOperacao("Depósito de R$${formatar(valor)} realizado.")
        println("Depósito realizado com sucesso!")
    }

    // "open" porque contas específicas podem ter regras próprias de saque
    // (ex.: Conta Corrente com cheque especial, Conta Investimento com taxa de resgate)
    open fun sacar(valor: Double) {
        if (valor <= 0.0) {
            throw ValorInvalidoException("Saque inválido! O valor deve ser maior que zero.")
        }
        if (valor > saldo) {
            throw SaldoInsuficienteException("Saldo insuficiente para realizar esta operação!")
        }
        saldo -= valor
        registrarOperacao("Saque de R$${formatar(valor)} realizado.")
        println("Saque realizado com sucesso!")
    }

    fun consultarSaldo(): Double = saldo

    open fun exibirDados() {
        println("Cliente: $titular")
        println("Conta: $numero")
        println("Saldo Atual: R$${formatar(saldo)}")
    }

    fun transferir(valor: Double, contaDestino: Conta) {
        if (valor <= 0.0) {
            throw ValorInvalidoException("Transferência inválida! O valor deve ser maior que zero.")
        }
        if (valor > saldo) {
            throw SaldoInsuficienteException("Saldo insuficiente para realizar a transferência!")
        }
        saldo -= valor
        contaDestino.receberTransferencia(valor, this)
        registrarOperacao("Transferência de R$${formatar(valor)} enviada para a conta ${contaDestino.numero}.")
        println("Transferência realizada com sucesso!")
    }

    protected fun receberTransferencia(valor: Double, contaOrigem: Conta) {
        saldo += valor
        registrarOperacao("Transferência de R$${formatar(valor)} recebida da conta ${contaOrigem.numero}.")
    }

    protected fun registrarOperacao(descricao: String) {
        historico.add(descricao)
    }

    fun exibirHistorico() {
        println("Conta $numero - $titular")
        if (historico.isEmpty()) {
            println("Nenhuma operação registrada.")
        } else {
            historico.forEach { println("- $it") }
        }
    }

    // Cada tipo de conta define sua própria regra de taxa/rendimento (polimorfismo)
    abstract fun aplicarRegraEspecifica()

    protected fun formatar(valor: Double): String = String.format(Locale.US, "%.2f", valor)
}

// ============================================================
// CONTA CORRENTE
// Regra específica: taxa de manutenção mensal e limite de
// cheque especial para saques.
// ============================================================
class ContaCorrente(
    numero: Int,
    titular: String,
    saldoInicial: Double = 0.0,
    private val limiteChequeEspecial: Double = 0.0,
    private val taxaManutencao: Double = 30.0
) : Conta(numero, titular, saldoInicial) {

    override fun sacar(valor: Double) {
        if (valor <= 0.0) {
            throw ValorInvalidoException("Saque inválido! O valor deve ser maior que zero.")
        }
        if (valor > saldo + limiteChequeEspecial) {
            throw SaldoInsuficienteException("Saldo insuficiente para realizar esta operação!")
        }
        saldo -= valor
        registrarOperacao("Saque de R$${formatar(valor)} realizado.")
        println("Saque realizado com sucesso!")
    }

    override fun exibirDados() {
        super.exibirDados()
        println("Tipo: Conta Corrente")
        println("Limite de Cheque Especial: R$${formatar(limiteChequeEspecial)}")
    }

    override fun aplicarRegraEspecifica() {
        println("Aplicando taxa mensal...")
        // nunca deixa o saldo negativo: cobra no máximo o que existir em conta
        val valorTaxa = minOf(taxaManutencao, saldo)
        saldo -= valorTaxa
        registrarOperacao("Taxa de manutenção de R$${formatar(valorTaxa)} aplicada.")
        println("Novo saldo: R$${formatar(saldo)}")
    }
}

// ============================================================
// CONTA POUPANÇA
// Regra específica: rendimento mensal sobre o saldo.
// ============================================================
class ContaPoupanca(
    numero: Int,
    titular: String,
    saldoInicial: Double = 0.0,
    private val taxaRendimento: Double = 0.005 // 0,5% ao mês
) : Conta(numero, titular, saldoInicial) {

    override fun exibirDados() {
        super.exibirDados()
        println("Tipo: Conta Poupança")
        println("Taxa de Rendimento: ${taxaRendimento * 100}% ao mês")
    }

    override fun aplicarRegraEspecifica() {
        println("Aplicando rendimento mensal...")
        val rendimento = saldo * taxaRendimento
        saldo += rendimento
        registrarOperacao("Rendimento de R$${formatar(rendimento)} aplicado.")
        println("Novo saldo: R$${formatar(saldo)}")
    }
}

// ============================================================
// CONTA INVESTIMENTO
// Mostra que o sistema é facilmente extensível: um novo tipo
// de conta (pedido no "Desafio") com regras totalmente
// diferentes, sem alterar nenhuma classe já existente.
// ============================================================
class ContaInvestimento(
    numero: Int,
    titular: String,
    saldoInicial: Double = 0.0,
    private val taxaRendimento: Double = 0.012,       // 1,2% ao mês
    private val taxaResgateAntecipado: Double = 0.02   // 2% sobre o valor sacado
) : Conta(numero, titular, saldoInicial) {

    override fun sacar(valor: Double) {
        if (valor <= 0.0) {
            throw ValorInvalidoException("Saque inválido! O valor deve ser maior que zero.")
        }
        val taxa = valor * taxaResgateAntecipado
        val valorTotal = valor + taxa
        if (valorTotal > saldo) {
            throw SaldoInsuficienteException("Saldo insuficiente para realizar esta operação!")
        }
        saldo -= valorTotal
        registrarOperacao("Resgate de R$${formatar(valor)} realizado (taxa de resgate: R$${formatar(taxa)}).")
        println("Saque realizado com sucesso! (taxa de resgate antecipado aplicada)")
    }

    override fun exibirDados() {
        super.exibirDados()
        println("Tipo: Conta Investimento")
        println("Taxa de Rendimento: ${taxaRendimento * 100}% ao mês")
    }

    override fun aplicarRegraEspecifica() {
        println("Aplicando rendimento de investimento...")
        val rendimento = saldo * taxaRendimento
        saldo += rendimento
        registrarOperacao("Rendimento de investimento de R$${formatar(rendimento)} aplicado.")
        println("Novo saldo: R$${formatar(saldo)}")
    }
}

// ============================================================
// FUNÇÃO AUXILIAR
// Centraliza o tratamento de exceções das operações bancárias
// ============================================================
private fun executarOperacao(operacao: () -> Unit) {
    try {
        operacao()
    } catch (e: ValorInvalidoException) {
        println(e.message)
    } catch (e: SaldoInsuficienteException) {
        println(e.message)
    }
}

private fun exibirCabecalho(titulo: String) {
    println("====================================")
    println(titulo)
    println("====================================")
}

// ============================================================
// MAIN
// Todas as contas e operações são criadas diretamente aqui,
// sem entrada de dados pelo teclado.
// ============================================================
fun main() {
    exibirCabecalho("BANCO BYTEBANK EVOLUTION")

    // Criação de múltiplas contas de tipos diferentes
    val contaMaria = ContaCorrente(1001, "Maria Silva", 2500.0, limiteChequeEspecial = 500.0, taxaManutencao = 30.0)
    val contaJoao = ContaPoupanca(1002, "João Pereira", 1000.0, taxaRendimento = 0.005)
    val contaAna = ContaInvestimento(1003, "Ana Souza", 5000.0, taxaRendimento = 0.012)

    contaMaria.exibirDados()
    println()

    // Movimentações financeiras da conta de Maria
    executarOperacao { contaMaria.depositar(500.0) }
    executarOperacao { contaMaria.sacar(200.0) }
    executarOperacao { contaMaria.sacar(10000.0) }    // saldo insuficiente -> bloqueado
    executarOperacao { contaMaria.depositar(-50.0) }  // depósito inválido -> bloqueado

    println()
    contaMaria.aplicarRegraEspecifica() // taxa mensal da conta corrente

    println()
    executarOperacao { contaMaria.transferir(100.0, contaJoao) }

    println()
    println("Dados atualizados após as operações:")
    println("------------------------------------")
    contaMaria.exibirDados()
    println("------------------------------------")
    contaJoao.exibirDados()

    // Demonstração de POLIMORFISMO:
    // a mesma chamada aplicarRegraEspecifica() produz comportamentos diferentes
    // dependendo do tipo real de cada conta.
    println()
    exibirCabecalho("REGRAS ESPECÍFICAS DE CADA CONTA")
    val contas: List<Conta> = listOf(contaMaria, contaJoao, contaAna)
    for (conta in contas) {
        println("------------------------------------")
        conta.exibirDados()
        conta.aplicarRegraEspecifica()
    }

    // Histórico simples de operações de todas as contas
    println()
    exibirCabecalho("HISTÓRICO DE OPERAÇÕES")
    for (conta in contas) {
        conta.exibirHistorico()
        println()
    }
}