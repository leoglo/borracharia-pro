function app() {
    return {
        produtos: [],
        loading: true,
        modalAberto: false,
        editando: false,
        form: {
            id: null,
            codigoProduto: "",
            descricao: "",
            marca: "",
            precoVenda: null,
            quantidadeEstoque: null
        },

        async init() {
            await this.carregarProdutos();
        },

        async carregarProdutos() {
            try {
                this.loading = true;
                const res = await fetch("/produtos");
                if (!res.ok) throw new Error("Erro ao carregar produtos.");
                this.produtos = await res.json();
            } catch (e) {
                alert("Erro ao carregar produtos!");
                console.error(e);
            } finally {
                this.loading = false;
            }
        },

        openModal() {
            this.modalAberto = true;
            this.editando = false;
            this.form = {
                id: null,
                codigoProduto: "",
                descricao: "",
                marca: "",
                precoVenda: null,
                quantidadeEstoque: null
            };
        },

        fecharModal() {
            this.modalAberto = false;
        },

        editar(produto) {
            this.editando = true;
            this.modalAberto = true;

            this.form = {
                id: produto.id,
                codigoProduto: produto.codigoProduto,
                descricao: produto.descricao,
                marca: produto.marca,
                precoVenda: produto.precoVenda,
                quantidadeEstoque: produto.quantidadeEstoque
            };
        },

        async excluir(id) {
            if (!confirm("Deseja realmente excluir este produto?")) return;

            try {
                const res = await fetch(`/produtos/${id}`, {
                    method: "DELETE"
                });

                if (!res.ok) throw new Error("Erro ao excluir");

                this.produtos = this.produtos.filter(p => p.id !== id);

            } catch (e) {
                alert("Erro ao excluir produto!");
                console.error(e);
            }
        },

        async salvar() {
            try {
                const metodo = this.editando ? "PUT" : "POST";
                const url = this.editando ? `/produtos/${this.form.id}` : "/produtos";

                const res = await fetch(url, {
                    method: metodo,
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(this.form)
                });

                if (!res.ok) {
                    const erro = await res.text();
                    alert("Erro: " + erro);
                    throw new Error("Falha ao salvar produto");
                }

                await this.carregarProdutos();
                this.fecharModal();

            } catch (e) {
                alert("Erro ao salvar produto!");
                console.error(e);
            }
        },

        // Formatação de preço
        formatarPreco(valor) {
            if (valor == null) return "0,00";
            return Number(valor).toFixed(2).replace(".", ",");
        }
    };
}
