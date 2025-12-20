import { useState, useEffect } from 'react';
import { ArrowLeft, Save, Plus, Trash2, Car, UserPlus, X, Search } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../services/api';


export default function OrcamentoFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [cliente, setCliente] = useState(null);
  const [veiculoSelecionado, setVeiculoSelecionado] = useState(null);
  const [clientesFiltrados, setClientesFiltrados] = useState([]);
  const [buscaCliente, setBuscaCliente] = useState('');
  const [mostrarDropdown, setMostrarDropdown] = useState(false);
  const [veiculosDoCliente, setVeiculosDoCliente] = useState([]);
  const [itens, setItens] = useState([
    { codigo: '', descricao: '', quantidade: 1, valorUnitario: 0, total: 0 }
  ]);
  const [produtos, setProdutos] = useState([]);
  const [buscaProduto, setBuscaProduto] = useState('');
  const [produtosFiltrados, setProdutosFiltrados] = useState([]);
  const [indiceProdutoAtivo, setIndiceProdutoAtivo] = useState(null);
  const [desconto, setDesconto] = useState(0);
  const [loading, setLoading] = useState(false);
  const [showModalCliente, setShowModalCliente] = useState(false);
  const [showModalVeiculos, setShowModalVeiculos] = useState(false);
  const [orcamentoNum, setOrcamentoNum] = useState(isEdit ? 'Carregando...' : 'Novo');
  const [dataOrcamento, setDataOrcamento] = useState(new Date().toISOString().split('T')[0]);

  // --- NOVOS ESTADOS PARA A BUSCA ---
  const [showModalBuscaOrcamento, setShowModalBuscaOrcamento] = useState(false);
  const [termoBuscaOrcamento, setTermoBuscaOrcamento] = useState('');
  const [orcamentosEncontrados, setOrcamentosEncontrados] = useState([]);
  // ----------------------------------

  // Form novo cliente
  const [novoCliente, setNovoCliente] = useState({
    nome: '', cpf: '', telefone: '', email: '',
    rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: ''
  });

  // Form novo veículo
  const [novoVeiculo, setNovoVeiculo] = useState({
    placa: '', marca: '', modelo: '', ano: '', cor: ''
  });

  const subtotal = itens.reduce((acc, i) => acc + i.total, 0);
  const descontoEmReais = (subtotal * desconto) / 100;
  const totalGeral = subtotal - descontoEmReais;

  // Buscar produtos do banco de dados
  useEffect(() => {
    const carregarProdutos = async () => {
      try {
        const res = await api.get('/produtos');
        setProdutos(res.data);
      } catch (err) {
        console.error('Erro ao carregar produtos:', err);
      }
    };
    carregarProdutos();
  }, []);

  // Filtrar produtos conforme busca
  const buscarProdutos = (termo, index) => {
    setBuscaProduto(termo);
    setIndiceProdutoAtivo(index);

    if (termo.trim().length < 2) {
      setProdutosFiltrados([]);
      return;
    }

    const filtrados = produtos.filter(p =>
      p.codigoProduto?.toLowerCase().includes(termo.toLowerCase()) ||
      p.codigo?.toLowerCase().includes(termo.toLowerCase()) ||
      p.descricao?.toLowerCase().includes(termo.toLowerCase())
    );
    setProdutosFiltrados(filtrados);
  };

  const selecionarProduto = (produto, index) => {
    const novos = [...itens];
    novos[index].codigo = produto.codigoProduto || produto.codigo || '';
    novos[index].descricao = produto.descricao || produto.nome || '';
    // Busca o preço de venda do produto
    novos[index].valorUnitario = Number(produto.precoVenda || produto.preco) || 0;

    const qtd = Number(novos[index].quantidade) || 0;
    const unit = Number(novos[index].valorUnitario) || 0;
    novos[index].total = qtd * unit;

    setItens(novos);
    setProdutosFiltrados([]);
    setBuscaProduto('');
    setIndiceProdutoAtivo(null);
  };

  // Carregar dados do Orçamento para edição
  useEffect(() => {
    if (isEdit) {
      const fetchOrcamento = async () => {
        setLoading(true);
        try {
          const res = await api.get(`/orcamentos/${id}`);
          const orcamento = res.data;

          setCliente(orcamento.cliente);
          setBuscaCliente(orcamento.cliente.nome);
          setItens(orcamento.itens);
          setDesconto(orcamento.desconto || 0);
          setVeiculoSelecionado(orcamento.veiculo);
          setOrcamentoNum(orcamento.numeroOrcamento || `ORC-${id}`);
          setDataOrcamento(orcamento.dataAbertura.split('T')[0]);

          const resV = await api.get(`/veiculos/cliente/${orcamento.cliente.id}`);
          setVeiculosDoCliente(resV.data);

        } catch (err) {
          console.error('Erro ao carregar orçamento:', err);
          alert('Erro ao carregar os dados do orçamento para edição.');
        } finally {
          setLoading(false);
        }
      };
      fetchOrcamento();
    }
  }, [id, isEdit]);

  // Busca clientes no backend conforme digita (com debounce)
  useEffect(() => {
    const buscarClientesNoBackend = async () => {
      if (buscaCliente.trim().length < 2) {
        setClientesFiltrados([]);
        return;
      }

      try {
        const res = await api.get(`/clientes/buscar?termo=${encodeURIComponent(buscaCliente)}`);
        setClientesFiltrados(res.data);
      } catch (err) {
        console.error('Erro ao buscar clientes:', err);
        setClientesFiltrados([]);
      }
    };

    const timeoutId = setTimeout(buscarClientesNoBackend, 300);
    return () => clearTimeout(timeoutId);
  }, [buscaCliente]);

  const handleBuscaCliente = (texto) => {
    setBuscaCliente(texto);
    setMostrarDropdown(true);
  };

  const selecionarCliente = async (clienteSelecionado) => {
    setCliente(clienteSelecionado);
    setBuscaCliente(clienteSelecionado.nome);
    setMostrarDropdown(false);

    try {
      const resV = await api.get(`/veiculos/cliente/${clienteSelecionado.id}`);
      setVeiculosDoCliente(resV.data);
      setVeiculoSelecionado(null);
    } catch (err) {
      console.error('Erro ao carregar veículos:', err);
      alert('Erro ao carregar veículos do cliente');
    }
  };

  const atualizarItem = (index, campo, valor) => {
    const novos = [...itens];
    novos[index][campo] = valor;

    if (campo === 'quantidade' || campo === 'valorUnitario') {
      const qtd = Number(novos[index].quantidade) || 0;
      const unit = Number(novos[index].valorUnitario) || 0;
      novos[index].total = qtd * unit;
    }
    setItens(novos);
  };

  const adicionarItem = () => {
    setItens([...itens, { codigo: '', descricao: '', quantidade: 1, valorUnitario: 0, total: 0 }]);
  };

  const removerItem = (index) => {
    if (itens.length === 1) return;
    setItens(itens.filter((_, i) => i !== index));
  };

  const handleCadastrarCliente = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await api.post('/clientes', novoCliente);
      setCliente(res.data);
      setBuscaCliente(res.data.nome);
      setVeiculosDoCliente([]);
      setVeiculoSelecionado(null);

      setShowModalCliente(false);
      setNovoCliente({
        nome: '', cpf: '', telefone: '', email: '',
        rua: '', numero: '', bairro: '', cidade: '', estado: '', cep: ''
      });
      alert('Cliente cadastrado com sucesso!');
    } catch (err) {
      alert('Erro ao cadastrar cliente');
    } finally {
      setLoading(false);
    }
  };

  const handleCadastrarVeiculo = async (e) => {
    e.preventDefault();
    if (!cliente) return alert('Selecione um cliente primeiro');

    setLoading(true);
    try {
      const payload = {
        ...novoVeiculo,
        clienteId: cliente.id
      };
      const res = await api.post('/veiculos', payload);
      setVeiculosDoCliente([...veiculosDoCliente, res.data]);
      setVeiculoSelecionado(res.data);
      setShowModalVeiculos(false);
      setNovoVeiculo({ placa: '', marca: '', modelo: '', ano: '', cor: '' });
      alert('Veículo cadastrado com sucesso!');
    } catch (err) {
      alert('Erro ao cadastrar veículo');
    } finally {
      setLoading(false);
    }
  };

  const buscarOrcamentosExistentes = async () => {
    if (termoBuscaOrcamento.trim().length < 2) {
      alert('Digite pelo menos 2 caracteres para buscar');
      return;
    }

    setLoading(true);
    try {
      const res = await api.get(`/orcamentos/buscar?termo=${encodeURIComponent(termoBuscaOrcamento)}`);
      setOrcamentosEncontrados(res.data);
    } catch (err) {
      console.error('Erro ao buscar orçamentos:', err);
      alert('Erro ao buscar orçamentos');
      setOrcamentosEncontrados([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!cliente) return alert('Selecione um cliente');
    if (!veiculoSelecionado) return alert('Selecione um veículo');

    setLoading(true);
    try {
      const payload = {
        clienteId: cliente.id,
        veiculoId: veiculoSelecionado.id,
        itens: itens.map(item => ({ ...item, total: Number(item.total) })),
        desconto: Number(desconto),
        total: totalGeral,
        dataAbertura: dataOrcamento
      };

      if (isEdit) {
        await api.put(`/orcamentos/${id}`, payload);
      } else {
        await api.post('/orcamentos', payload);
      }

      alert('Orçamento salvo com sucesso!');
      navigate('/orcamentos');
    } catch (err) {
      console.error("Erro no submit:", err);
      alert('Erro ao salvar orçamento');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-black p-4 md:p-6">
      <div className="max-w-7xl mx-auto">

        {/* HEADER */}
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8">
          <div className="flex items-center gap-4">
            <button
              onClick={() => navigate(-1)}
              className="p-3 bg-gray-800 rounded-xl hover:bg-gray-700 transition shadow-lg"
            >
              <ArrowLeft className="w-6 h-6 text-white" />
            </button>
            <h1 className="text-2xl md:text-3xl lg:text-4xl font-extrabold text-white flex items-center gap-3">
              <Car className="w-8 h-8 md:w-10 md:h-10 text-cyan-400" />
              {isEdit ? 'EDITAR ORÇAMENTO' : 'NOVO ORÇAMENTO'}
            </h1>
          </div>

          {/* INFORMAÇÕES NO HEADER */}
          <div className="flex flex-wrap gap-4">
            <div className="flex items-end gap-2">
              <button
                type="button"
                onClick={() => setShowModalBuscaOrcamento(true)}
                className="p-3 bg-cyan-600 hover:bg-cyan-500 text-black rounded-xl transition shadow-lg flex items-center justify-center mb-0.5"
                title="Pesquisar Orçamento"
              >
                <Search className="w-5 h-5" />
              </button>

              <div className="bg-gray-800/80 rounded-xl px-4 py-3 border border-gray-700">
                <label className="text-xs text-gray-400 block mb-1 uppercase font-bold">Nº Orçamento</label>
                <input
                  type="text"
                  value={orcamentoNum}
                  readOnly
                  className="bg-transparent text-cyan-300 font-bold text-sm outline-none w-32"
                />
              </div>
            </div>
            <div className="bg-gray-800/80 rounded-xl px-4 py-3 border border-gray-700">
              <label className="text-xs text-gray-400 block mb-1">DATA</label>
              <input
                type="date"
                value={dataOrcamento}
                onChange={(e) => setDataOrcamento(e.target.value)}
                className="bg-transparent text-white font-bold text-sm outline-none"
              />
            </div>
          </div>
        </div>

        {loading && isEdit && (
          <div className="text-center py-4 text-cyan-400 font-semibold">Carregando dados do orçamento...</div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">

          {/* DADOS DO CLIENTE */}
          <div className="bg-gray-900/80 backdrop-blur-lg rounded-2xl p-4 md:p-6 border border-gray-700 shadow-2xl">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
              <h2 className="text-xl md:text-2xl font-bold text-cyan-400">DADOS DO CLIENTE</h2>
              <button
                type="button"
                onClick={() => setShowModalCliente(true)}
                className="bg-green-600 hover:bg-green-500 text-white font-bold py-2 px-4 rounded-xl flex items-center gap-2 text-sm w-fit"
              >
                <UserPlus className="w-4 h-4" /> Novo Cliente
              </button>
            </div>

            {/* Cliente Busca + CPF + Telefone */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              <div className="relative">
                <label className="block text-sm text-gray-300 mb-2">Cliente:</label>
                <input
                  type="text"
                  value={buscaCliente}
                  onChange={(e) => handleBuscaCliente(e.target.value)}
                  onFocus={() => setMostrarDropdown(true)}
                  onBlur={() => setTimeout(() => setMostrarDropdown(false), 200)}
                  placeholder="Digite o nome, CPF ou telefone..."
                  className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white focus:border-cyan-500 outline-none"
                  required
                />

                {/* Dropdown de resultados */}
                {mostrarDropdown && clientesFiltrados.length > 0 && (
                  <div className="absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded-xl shadow-2xl max-h-60 overflow-y-auto">
                    {clientesFiltrados.map((c) => (
                      <button
                        key={c.id}
                        type="button"
                        onClick={() => selecionarCliente(c)}
                        className="w-full text-left px-4 py-3 hover:bg-gray-700 transition border-b border-gray-700 last:border-b-0"
                      >
                        <p className="font-bold text-white">{c.nome}</p>
                        <p className="text-xs text-gray-400">CPF: {c.cpf} • {c.telefone}</p>
                      </button>
                    ))}
                  </div>
                )}

                {/* Mensagem quando não encontra */}
                {mostrarDropdown && buscaCliente.trim().length >= 2 && clientesFiltrados.length === 0 && (
                  <div className="absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded-xl shadow-2xl p-4">
                    <p className="text-gray-400 text-sm">Nenhum cliente encontrado</p>
                  </div>
                )}
              </div>

              <div>
                <label className="block text-sm text-gray-300 mb-2">CPF:</label>
                <input
                  type="text"
                  value={cliente?.cpf || ''}
                  placeholder="000.000.000-00"
                  readOnly
                  className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                />
              </div>

              <div>
                <label className="block text-sm text-gray-300 mb-2">Telefone:</label>
                <input
                  type="text"
                  value={cliente?.telefone || ''}
                  placeholder="(00) 00000-0000"
                  readOnly
                  className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                />
              </div>
            </div>

            {/* Endereço */}
            {cliente && (
              <>
                <h3 className="text-base md:text-lg font-bold text-cyan-400 mb-3 mt-4">Endereço</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
                  <div className="sm:col-span-2 md:col-span-2 lg:col-span-2">
                    <label className="block text-sm text-gray-300 mb-2">Rua:</label>
                    <input
                      type="text"
                      value={cliente?.rua || ''}
                      readOnly
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                    />
                  </div>

                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Número:</label>
                    <input
                      type="text"
                      value={cliente?.numero || ''}
                      readOnly
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                    />
                  </div>

                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Bairro:</label>
                    <input
                      type="text"
                      value={cliente?.bairro || ''}
                      readOnly
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                    />
                  </div>

                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Cidade:</label>
                    <input
                      type="text"
                      value={cliente?.cidade || ''}
                      readOnly
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                    />
                  </div>

                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Estado:</label>
                    <input
                      type="text"
                      value={cliente?.estado || ''}
                      readOnly
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-xl px-4 py-3 text-gray-400"
                    />
                  </div>
                </div>
              </>
            )}

            {/* Veículo */}
            {cliente && (
              <div className="mt-6">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-3">
                  <label className="block text-sm font-bold text-cyan-300">VEÍCULO</label>
                  <button
                    type="button"
                    onClick={() => setShowModalVeiculos(true)}
                    className="bg-cyan-600 hover:bg-cyan-500 text-black font-bold py-2 px-4 rounded-xl flex items-center gap-2 text-sm w-fit"
                  >
                    <Plus className="w-4 h-4" /> Novo Veículo
                  </button>
                </div>
                <select
                  className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                  onChange={e => setVeiculoSelecionado(veiculosDoCliente.find(v => v.id === Number(e.target.value)))}
                  value={veiculoSelecionado?.id || ''}
                  required
                >
                  <option value="">Selecione o veículo...</option>
                  {veiculosDoCliente.map(v => (
                    <option key={v.id} value={v.id}>
                      {v.placa} • {v.marca} {v.modelo} ({v.cor})
                    </option>
                  ))}
                </select>
              </div>
            )}
          </div>

          {/* ITENS */}
          <div className="bg-gray-900/80 backdrop-blur-lg rounded-2xl p-4 md:p-6 border border-gray-700 shadow-2xl">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
              <h2 className="text-xl md:text-2xl font-bold text-cyan-400">ITENS DO ORÇAMENTO</h2>
              <button type="button" onClick={adicionarItem} className="bg-cyan-600 hover:bg-cyan-500 text-black font-bold py-2 px-4 rounded-xl flex items-center gap-2 text-sm w-fit">
                <Plus className="w-4 h-4" /> Adicionar Item
              </button>
            </div>

            <div className="overflow-x-auto -mx-4 md:mx-0">
              <div className="inline-block min-w-full align-middle">
                <table className="w-full min-w-[800px]">
                  <thead>
                    <tr className="text-left text-cyan-400 text-xs md:text-sm uppercase border-b-2 border-cyan-400">
                      <th className="pb-3 px-2">CÓDIGO</th>
                      <th className="pb-3 px-2">DESCRIÇÃO</th>
                      <th className="pb-3 px-2">QTD</th>
                      <th className="pb-3 px-2">UNITÁRIO</th>
                      <th className="pb-3 px-2 text-right">TOTAL</th>
                      <th className="pb-3 px-2"></th>
                    </tr>
                  </thead>
                  <tbody>
                    {itens.map((item, i) => (
                      <tr key={i} className="border-b border-gray-700">
                        <td className="py-3 px-2">
                          <input
                            className="bg-gray-800/70 border border-gray-700 rounded-lg px-2 py-2 w-full text-white text-sm"
                            value={item.codigo}
                            onChange={e => atualizarItem(i, 'codigo', e.target.value)}
                            placeholder="Código"
                          />
                        </td>
                        <td className="py-3 px-2 relative">
                          <input
                            className="bg-gray-800/70 border border-gray-700 rounded-lg px-2 py-2 w-full text-white text-sm"
                            value={indiceProdutoAtivo === i ? buscaProduto : item.descricao}
                            onChange={e => {
                              buscarProdutos(e.target.value, i);
                              atualizarItem(i, 'descricao', e.target.value);
                            }}
                            onFocus={() => {
                              setIndiceProdutoAtivo(i);
                              setBuscaProduto(item.descricao);
                            }}
                            onBlur={() => {
                              setTimeout(() => {
                                setIndiceProdutoAtivo(null);
                                setProdutosFiltrados([]);
                              }, 200);
                            }}
                            placeholder="Digite para buscar produto..."
                          />

                          {/* Dropdown de produtos */}
                          {indiceProdutoAtivo === i && produtosFiltrados.length > 0 && (
                            <div className="absolute z-20 w-full mt-1 bg-gray-800 border border-cyan-500 rounded-lg shadow-2xl max-h-60 overflow-y-auto">
                              {produtosFiltrados.map((p) => (
                                <button
                                  key={p.id}
                                  type="button"
                                  onClick={() => selecionarProduto(p, i)}
                                  className="w-full text-left px-3 py-2 hover:bg-gray-700 transition border-b border-gray-700 last:border-b-0"
                                >
                                  <p className="font-bold text-white text-sm">{p.descricao || p.nome}</p>
                                  <p className="text-xs text-gray-400">
                                    Código: {p.codigoProduto || p.codigo} • R$ {Number(p.precoVenda || 0).toFixed(2).replace('.', ',')}
                                  </p>
                                </button>
                              ))}
                            </div>
                          )}
                        </td>
                        <td className="py-3 px-2">
                          <input
                            type="number"
                            min="1"
                            className="bg-gray-800/70 border border-gray-700 rounded-lg px-2 py-2 w-16 text-center text-white text-sm"
                            value={item.quantidade}
                            onChange={e => atualizarItem(i, 'quantidade', Number(e.target.value))}
                          />
                        </td>
                        <td className="py-3 px-2">
                          <input
                            type="number"
                            step="0.01"
                            className="bg-gray-800/70 border border-gray-700 rounded-lg px-2 py-2 w-24 text-white text-sm"
                            value={item.valorUnitario}
                            onChange={e => atualizarItem(i, 'valorUnitario', Number(e.target.value))}
                          />
                        </td>
                        <td className="py-3 px-2 text-right font-bold text-cyan-300 text-sm">
                          R$ {item.total.toFixed(2).replace('.', ',')}
                        </td>
                        <td className="py-3 px-2 text-center">
                          {itens.length > 1 && (
                            <button type="button" onClick={() => removerItem(i)} className="text-red-400 hover:bg-red-900/30 p-2 rounded">
                              <Trash2 className="w-4 h-4" />
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot className="text-sm md:text-base font-bold">
                    <tr>
                      <td colSpan={4} className="text-right py-3 px-2 text-white">SUBTOTAL</td>
                      <td className="text-right text-cyan-300 py-3 px-2">R$ {subtotal.toFixed(2).replace('.', ',')}</td>
                      <td></td>
                    </tr>
                    <tr>
                      <td colSpan={4} className="text-right py-3 px-2">
                        <label className="text-white mr-3">DESCONTO:</label>
                        <input
                          type="number"
                          step="0.01"
                          value={desconto}
                          onChange={e => setDesconto(Number(e.target.value))}
                          placeholder="0,00"
                          className="bg-gray-800/70 border border-gray-700 rounded-lg px-2 py-2 w-24 text-right text-white text-sm"
                        />
                      </td>
                      <td className="text-right text-red-400 py-3 px-2">-  {desconto.toFixed(2).replace('.', ',')} % </td>
                      <td></td>
                    </tr>
                    <tr className="text-lg md:text-xl font-extrabold text-cyan-400">
                      <td colSpan={4} className="text-right py-4 px-2">TOTAL GERAL</td>
                      <td className="text-right py-4 px-2">R$ {totalGeral.toFixed(2).replace('.', ',')}</td>
                      <td></td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          </div>

          {/* BOTÕES */}
          <div className="flex flex-col sm:flex-row justify-center gap-4 pt-6">
            <button type="button" onClick={() => navigate(-1)} className="px-8 py-3 bg-red-600 hover:bg-red-500 text-white text-base md:text-lg font-bold rounded-xl transition shadow-2xl">
              CANCELAR
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-8 py-3 bg-cyan-500 hover:bg-cyan-400 text-black text-base md:text-lg font-bold rounded-xl transition shadow-2xl flex items-center justify-center gap-3 disabled:opacity-50"
            >
              {loading ? 'SALVANDO...' : 'SALVAR ORÇAMENTO'}
              <Save className="w-5 h-5" />
            </button>
          </div>

        </form>

        {/* MODAL CADASTRAR CLIENTE */}
        {showModalCliente && (
          <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
            <div className="bg-gray-900 rounded-2xl border border-gray-700 w-full max-w-3xl max-h-[90vh] overflow-y-auto">
              <div className="sticky top-0 bg-gray-900 border-b border-gray-700 p-4 md:p-6 flex items-center justify-between">
                <h2 className="text-xl md:text-2xl font-bold text-cyan-400">NOVO CLIENTE</h2>
                <button onClick={() => setShowModalCliente(false)} className="text-gray-400 hover:text-white">
                  <X className="w-6 h-6" />
                </button>
              </div>
              <form onSubmit={handleCadastrarCliente} className="p-4 md:p-6 space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <label className="block text-sm text-gray-300 mb-2">Nome Completo *</label>
                    <input
                      type="text"
                      required
                      value={novoCliente.nome}
                      onChange={e => setNovoCliente({ ...novoCliente, nome: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">CPF *</label>
                    <input
                      type="text"
                      required
                      value={novoCliente.cpf}
                      onChange={e => setNovoCliente({ ...novoCliente, cpf: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Telefone *</label>
                    <input
                      type="text"
                      required
                      value={novoCliente.telefone}
                      onChange={e => setNovoCliente({ ...novoCliente, telefone: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div className="md:col-span-2">
                    <label className="block text-sm text-gray-300 mb-2">Email</label>
                    <input
                      type="email"
                      value={novoCliente.email}
                      onChange={e => setNovoCliente({ ...novoCliente, email: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                </div>

                <h3 className="text-lg font-bold text-cyan-400 mt-6 mb-3">Endereço</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                  <div className="sm:col-span-2">
                    <label className="block text-sm text-gray-300 mb-2">Rua</label>
                    <input
                      type="text"
                      value={novoCliente.rua}
                      onChange={e => setNovoCliente({ ...novoCliente, rua: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Número</label>
                    <input
                      type="text"
                      value={novoCliente.numero}
                      onChange={e => setNovoCliente({ ...novoCliente, numero: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Bairro</label>
                    <input
                      type="text"
                      value={novoCliente.bairro}
                      onChange={e => setNovoCliente({ ...novoCliente, bairro: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Cidade</label>
                    <input
                      type="text"
                      value={novoCliente.cidade}
                      onChange={e => setNovoCliente({ ...novoCliente, cidade: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Estado</label>
                    <input
                      type="text"
                      maxLength={2}
                      value={novoCliente.estado}
                      onChange={e => setNovoCliente({ ...novoCliente, estado: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">CEP</label>
                    <input
                      type="text"
                      value={novoCliente.cep}
                      onChange={e => setNovoCliente({ ...novoCliente, cep: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                </div>
                <div className="flex justify-end gap-4 pt-4">
                  <button
                    type="button"
                    onClick={() => setShowModalCliente(false)}
                    className="px-6 py-2 bg-gray-700 hover:bg-gray-600 text-white font-bold rounded-xl transition"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    disabled={loading}
                    className="px-6 py-2 bg-green-600 hover:bg-green-500 text-white font-bold rounded-xl transition flex items-center gap-2 disabled:opacity-50"
                  >
                    {loading ? 'Salvando...' : 'Cadastrar Cliente'}
                    <UserPlus className="w-4 h-4" />
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
       
        {/* MODAL CADASTRAR VEÍCULO */}
        {showModalVeiculos && cliente && (
          <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
            <div className="bg-gray-900 rounded-2xl border border-gray-700 w-full max-w-xl max-h-[90vh] overflow-y-auto">
              <div className="sticky top-0 bg-gray-900 border-b border-gray-700 p-4 md:p-6 flex items-center justify-between">
                <h2 className="text-xl md:text-2xl font-bold text-cyan-400">NOVO VEÍCULO PARA {cliente.nome}</h2>
                <button onClick={() => setShowModalVeiculos(false)} className="text-gray-400 hover:text-white">
                  <X className="w-6 h-6" />
                </button>
              </div>
              <form onSubmit={handleCadastrarVeiculo} className="p-4 md:p-6 space-y-4">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Placa *</label>
                    <input
                      type="text"
                      required
                      value={novoVeiculo.placa}
                      onChange={e => setNovoVeiculo({ ...novoVeiculo, placa: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white uppercase"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Marca *</label>
                    <input
                      type="text"
                      required
                      value={novoVeiculo.marca}
                      onChange={e => setNovoVeiculo({ ...novoVeiculo, marca: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Modelo *</label>
                    <input
                      type="text"
                      required
                      value={novoVeiculo.modelo}
                      onChange={e => setNovoVeiculo({ ...novoVeiculo, modelo: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div>
                    <label className="block text-sm text-gray-300 mb-2">Ano</label>
                    <input
                      type="number"
                      value={novoVeiculo.ano}
                      onChange={e => setNovoVeiculo({ ...novoVeiculo, ano: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                  <div className="sm:col-span-2">
                    <label className="block text-sm text-gray-300 mb-2">Cor</label>
                    <input
                      type="text"
                      value={novoVeiculo.cor}
                      onChange={e => setNovoVeiculo({ ...novoVeiculo, cor: e.target.value })}
                      className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 py-3 text-white"
                    />
                  </div>
                </div>
                <div className="flex justify-end gap-4 pt-4">
                  <button
                    type="button"
                    onClick={() => setShowModalVeiculos(false)}
                    className="px-6 py-2 bg-gray-700 hover:bg-gray-600 text-white font-bold rounded-xl transition"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    disabled={loading}
                    className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 text-black font-bold rounded-xl transition flex items-center gap-2 disabled:opacity-50"
                  >
                    {loading ? 'Salvando...' : 'Cadastrar Veículo'}
                    <Car className="w-4 h-4" />
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

         {showModalBuscaOrcamento && (
          <div className="fixed inset-0 bg-black/80 backdrop-blur-sm flex items-center justify-center z-[60] p-4">
            <div className="bg-gray-900 border border-gray-700 rounded-2xl w-full max-w-2xl overflow-hidden shadow-2xl">
              <div className="p-6 border-b border-gray-800 flex justify-between items-center">
                <h2 className="text-xl font-bold text-cyan-400">BUSCAR ORÇAMENTO NO BANCO</h2>
                <button onClick={() => setShowModalBuscaOrcamento(false)} className="text-gray-400 hover:text-white">
                  <X className="w-6 h-6" />
                </button>
              </div>

              <div className="p-6">
                <div className="flex gap-2 mb-6">
                  <input
                    type="text"
                    placeholder="Digite nome do cliente ou CPF..."
                    className="flex-1 bg-gray-800 border border-gray-700 rounded-xl px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    value={termoBuscaOrcamento}
                    onChange={(e) => setTermoBuscaOrcamento(e.target.value)}
                  />
                  <button
                    onClick={buscarOrcamentosExistentes}
                    className="bg-cyan-500 hover:bg-cyan-400 text-black font-bold px-6 rounded-xl transition"
                  >
                    BUSCAR
                  </button>
                </div>

                <div className="max-h-80 overflow-y-auto space-y-2">
                  {orcamentosEncontrados.length > 0 ? (
                    orcamentosEncontrados.map((orc) => (
                      <button
                        key={orc.id}
                        onClick={() => {
                          navigate(`/orcamentos/editar/${orc.id}`); // Redireciona para a rota de edição
                          setShowModalBuscaOrcamento(false);
                        }}
                        className="w-full bg-gray-800/50 hover:bg-gray-700 p-4 rounded-xl border border-gray-700 flex justify-between items-center transition group"
                      >
                        <div className="text-left">
                          <p className="font-bold text-white group-hover:text-cyan-400">{orc.cliente?.nome || 'Cliente não identificado'}</p>
                          <p className="text-xs text-gray-400">Data: {new Date(orc.dataAbertura).toLocaleDateString('pt-BR')}</p>
                        </div>
                        <div className="text-right">
                          <p className="text-cyan-300 font-bold">R$ {Number(orc.total).toFixed(2).replace('.', ',')}</p>
                          <p className="text-[10px] text-gray-500 uppercase">Ver detalhes</p>
                        </div>
                      </button>
                    ))
                  ) : (
                    <p className="text-center text-gray-500 py-10">Nenhum orçamento encontrado.</p>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}

      </div>

    </div>

  );
}