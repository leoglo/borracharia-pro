import { useState, useEffect } from 'react';
import { ArrowLeft, Save } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../services/api';

export default function ProductFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [form, setForm] = useState({
    codigoProduto: '',
    codigoFabricante: '',
    descricao: '',
    complemento: '',
    marca: '',
    outrosInfo: '',
    classe: '',
    embalagem: '',
    unidade: '',
    quantidadeUnidade: '',
    localizacao: '',
    precoProduto: '',
    frete: '',
    custoMedio: '',
    ultimoReajuste: '',
    margemLucro: '',
    precoVenda: '',
    quantidadeEstoque: '',
    quantidadePedido: '',
    quantidadeAguardando: '',
    quantidadeMinima: '',
    codigoComissao: '',
    peso: '',
    ipiPercentual: '',
    diferencaIcmsPercentual: '',
    outrosPercentual: '',
    icmsPercentual: '',
    reducaoIcmsPercentual: '',
    icmsSubstituicaoPercentual: '',
    ipiPercentualFinal: '',
    checarEstoque: false,
  });

  const [loading, setLoading] = useState(false);
  
  // Armazena os valores originais para detectar mudanças
  const [valoresOriginais, setValoresOriginais] = useState({
    precoProduto: '',
    margemLucro: '',
    precoVenda: '',
  });

  // Calcula o Custo Médio automaticamente
  useEffect(() => {
    const precoProduto = parseFloat(form.precoProduto) || 0;
    const icms = parseFloat(form.icmsPercentual) || 0;
    const ipi = parseFloat(form.ipiPercentual) || 0;
    const outros = parseFloat(form.outrosPercentual) || 0;
    
    // Custo Médio = Preço Produto + Impostos
    const totalImpostos = (precoProduto * (icms + ipi + outros)) / 100;
    const custoMedioCalculado = precoProduto + totalImpostos;
    
    if (custoMedioCalculado > 0 && custoMedioCalculado !== parseFloat(form.custoMedio)) {
      setForm(prev => ({ 
        ...prev, 
        custoMedio: custoMedioCalculado.toFixed(2) 
      }));
    }
  }, [form.precoProduto, form.icmsPercentual, form.ipiPercentual, form.outrosPercentual]);

  // Calcula o Preço de Venda automaticamente baseado na margem
  useEffect(() => {
    const custoMedio = parseFloat(form.custoMedio) || 0;
    const margemLucro = parseFloat(form.margemLucro) || 0;
    
    if (custoMedio > 0 && margemLucro > 0) {
      // Preço de Venda = Custo Médio + (Custo Médio * Margem / 100)
      const precoVendaCalculado = custoMedio + (custoMedio * margemLucro / 100);
      
      if (precoVendaCalculado !== parseFloat(form.precoVenda)) {
        setForm(prev => ({ 
          ...prev, 
          precoVenda: precoVendaCalculado.toFixed(2) 
        }));
      }
    }
  }, [form.custoMedio, form.margemLucro]);

  // Carrega dados se for edição
  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      api.get(`/produtos/${id}`)
        .then(res => {
          console.log('✅ Dados recebidos do backend:', res.data);
          const data = res.data;
          setForm({
            codigoProduto: data.codigoProduto || '',
            codigoFabricante: data.codigoFabricante || '',
            descricao: data.descricao || '',
            complemento: data.complemento || '',
            marca: data.marca || '',
            outrosInfo: data.outrosInfo || '',
            classe: data.classe || '',
            embalagem: data.embalagem || '',
            unidade: data.unidade || '',
            quantidadeUnidade: data.quantidadeUnidade || '',
            localizacao: data.localizacao || '',
            precoProduto: data.precoProduto || '',
            frete: data.frete || '',
            custoMedio: data.custoMedio || '',
            ultimoReajuste: data.ultimoReajuste || '',
            margemLucro: data.margemLucro || '',
            precoVenda: data.precoVenda || '',
            quantidadeEstoque: data.quantidadeEstoque || '',
            quantidadePedido: data.quantidadePedido || '',
            quantidadeAguardando: data.quantidadeAguardando || '',
            quantidadeMinima: data.quantidadeMinima || '',
            codigoComissao: data.codigoComissao || '',
            peso: data.peso || '',
            ipiPercentual: data.ipiPercentual || '',
            diferencaIcmsPercentual: data.diferencaIcmsPercentual || '',
            outrosPercentual: data.outrosPercentual || '',
            icmsPercentual: data.icmsPercentual || '',
            reducaoIcmsPercentual: data.reducaoIcmsPercentual || '',
            icmsSubstituicaoPercentual: data.icmsSubstituicaoPercentual || '',
            ipiPercentualFinal: data.ipiPercentualFinal || '',
            checarEstoque: data.checarEstoque === 'S' || data.checarEstoque === true,
          });
          
          // Armazena os valores originais para detectar mudanças
          setValoresOriginais({
            precoProduto: data.precoProduto || '',
            margemLucro: data.margemLucro || '',
            precoVenda: data.precoVenda || '',
          });
        })
        .catch((err) => {
          console.error('❌ Erro ao carregar produto:', err);
          alert("Erro ao carregar produto: " + (err.response?.data?.message || err.message));
        })
        .finally(() => setLoading(false));
    }
  }, [id, isEdit]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({ 
      ...prev, 
      [name]: type === 'checkbox' ? checked : value 
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    // Verificar se houve alteração nos valores (preço, margem ou preço de venda)
    const houveAlteracaoPreco = isEdit && (
      parseFloat(form.precoProduto || 0) !== parseFloat(valoresOriginais.precoProduto || 0) ||
      parseFloat(form.margemLucro || 0) !== parseFloat(valoresOriginais.margemLucro || 0) ||
      parseFloat(form.precoVenda || 0) !== parseFloat(valoresOriginais.precoVenda || 0)
    );

    // Se houver alteração, atualiza a data de reajuste para hoje
    const dataReajuste = houveAlteracaoPreco 
      ? new Date().toISOString().split('T')[0] // Formato: YYYY-MM-DD
      : form.ultimoReajuste || null;

    // Preparar dados para envio ao backend
    const dataToSend = {
      codigoProduto: form.codigoProduto || null,
      codigoFabricante: form.codigoFabricante || null,
      descricao: form.descricao || null,
      complemento: form.complemento || null,
      marca: form.marca || null,
      outrosInfo: form.outrosInfo || null,
      classe: form.classe || null,
      embalagem: form.embalagem ? parseFloat(form.embalagem) : null,
      unidade: form.unidade || null,
      quantidadeUnidade: form.quantidadeUnidade ? parseFloat(form.quantidadeUnidade) : null,
      localizacao: form.localizacao || null,
      precoProduto: form.precoProduto ? parseFloat(form.precoProduto) : null,
      frete: form.frete ? parseFloat(form.frete) : null,
      custoMedio: form.custoMedio ? parseFloat(form.custoMedio) : null,
      ultimoReajuste: dataReajuste,
      margemLucro: form.margemLucro ? parseFloat(form.margemLucro) : null,
      precoVenda: form.precoVenda ? parseFloat(form.precoVenda) : null,
      quantidadeEstoque: form.quantidadeEstoque ? parseInt(form.quantidadeEstoque) : null,
      quantidadePedido: form.quantidadePedido ? parseInt(form.quantidadePedido) : null,
      quantidadeAguardando: form.quantidadeAguardando ? parseInt(form.quantidadeAguardando) : null,
      quantidadeMinima: form.quantidadeMinima ? parseInt(form.quantidadeMinima) : null,
      codigoComissao: form.codigoComissao || null,
      peso: form.peso ? parseFloat(form.peso) : null,
      ipiPercentual: form.ipiPercentual ? parseFloat(form.ipiPercentual) : null,
      diferencaIcmsPercentual: form.diferencaIcmsPercentual ? parseFloat(form.diferencaIcmsPercentual) : null,
      outrosPercentual: form.outrosPercentual ? parseFloat(form.outrosPercentual) : null,
      icmsPercentual: form.icmsPercentual ? parseFloat(form.icmsPercentual) : null,
      reducaoIcmsPercentual: form.reducaoIcmsPercentual ? parseFloat(form.reducaoIcmsPercentual) : null,
      icmsSubstituicaoPercentual: form.icmsSubstituicaoPercentual ? parseFloat(form.icmsSubstituicaoPercentual) : null,
      ipiPercentualFinal: form.ipiPercentualFinal ? parseFloat(form.ipiPercentualFinal) : null,
      checarEstoque: form.checarEstoque ? 'S' : 'N',
    };

    console.log('📤 Dados enviados ao backend:', dataToSend);
    
    if (houveAlteracaoPreco) {
      console.log('💰 Detectada alteração de preço! Data de reajuste atualizada para:', dataReajuste);
    }

    try {
      if (isEdit) {
        const response = await api.put(`/produtos/${id}`, dataToSend);
        console.log('✅ Resposta do PUT:', response.data);
        alert("Produto atualizado com sucesso!");
      } else {
        const response = await api.post('/produtos', dataToSend);
        console.log('✅ Resposta do POST:', response.data);
        alert("Produto cadastrado com sucesso!");
      }
      navigate('/produtos');
    } catch (err) {
      console.error('❌ Erro completo:', err);
      console.error('❌ Resposta do servidor:', err.response?.data);
      
      const errorMsg = err.response?.data?.message || err.response?.data || err.message;
      alert(`Erro ao salvar produto:\n${errorMsg}`);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEdit) {
    return (
      <div className="min-h-screen p-8 flex items-center justify-center">
        <div className="text-2xl text-cyan-400 animate-pulse">Carregando produto...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen p-4 sm:p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header */}
        <div className="flex items-center gap-4 mb-8">
          <button 
            onClick={() => navigate('/produtos')} 
            className="p-3 bg-gray-800 rounded-xl hover:bg-gray-700 transition"
            disabled={loading}
          >
            <ArrowLeft className="w-6 h-6 text-white" />
          </button>
          <h1 className="text-3xl font-extrabold text-white text-center flex-1">
            {isEdit ? 'EDITAR PRODUTO' : 'CADASTRO DE PRODUTO'}
          </h1>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Coluna 1: DADOS GERAIS e ESTOQUE */}
            <div className="lg:col-span-2">
              {/* DADOS GERAIS */}
              <div className="bg-gray-900/70 backdrop-blur rounded-2xl p-6 border border-gray-800 mb-6">
                <h2 className="text-xl font-bold text-cyan-400 mb-6">DADOS GERAIS</h2>
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {/* Codigo Produto */}
                  <div>
                    <label htmlFor="codigoProduto" className="text-sm text-gray-400 mb-1 block">
                      Código Produto *
                    </label>
                    <input 
                      id="codigoProduto" 
                      name="codigoProduto" 
                      type="text" 
                      value={form.codigoProduto}
                      onChange={handleChange}
                      required
                      maxLength={50}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Codigo Fabricante */}
                  <div>
                    <label htmlFor="codigoFabricante" className="text-sm text-gray-400 mb-1 block">
                      Código Fabricante
                    </label>
                    <input 
                      id="codigoFabricante" 
                      name="codigoFabricante" 
                      type="text"
                      value={form.codigoFabricante}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                </div>

                {/* Descricao */}
                <div className="mt-4">
                  <label htmlFor="descricao" className="text-sm text-gray-400 mb-1 block">
                    Descrição *
                  </label>
                  <input 
                    id="descricao" 
                    name="descricao" 
                    type="text"
                    value={form.descricao}
                    onChange={handleChange}
                    required
                    maxLength={200}
                    className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                  />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
                  {/* Marca */}
                  <div>
                    <label htmlFor="marca" className="text-sm text-gray-400 mb-1 block">Marca</label>
                    <input 
                      id="marca" 
                      name="marca" 
                      type="text"
                      value={form.marca}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Classe */}
                  <div>
                    <label htmlFor="classe" className="text-sm text-gray-400 mb-1 block">Classe</label>
                    <input 
                      id="classe" 
                      name="classe" 
                      type="text"
                      value={form.classe}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Localizacao */}
                  <div>
                    <label htmlFor="localizacao" className="text-sm text-gray-400 mb-1 block">Localização</label>
                    <input 
                      id="localizacao" 
                      name="localizacao" 
                      type="text"
                      value={form.localizacao}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Embalagem */}
                  <div>
                    <label htmlFor="embalagem" className="text-sm text-gray-400 mb-1 block">Embalagem</label>
                    <input 
                      id="embalagem" 
                      name="embalagem" 
                      type="number"
                      step="0.01"
                      value={form.embalagem}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Peso */}
                  <div>
                    <label htmlFor="peso" className="text-sm text-gray-400 mb-1 block">Peso (kg)</label>
                    <input 
                      id="peso" 
                      name="peso" 
                      type="number" 
                      step="0.01"
                      value={form.peso}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Outras Informações */}
                  <div>
                    <label htmlFor="outrosInfo" className="text-sm text-gray-400 mb-1 block">Outras Informações</label>
                    <input 
                      id="outrosInfo" 
                      name="outrosInfo" 
                      type="text"
                      value={form.outrosInfo}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                </div>
              </div>

              {/* ESTOQUE */}
              <div className="bg-gray-900/70 backdrop-blur rounded-2xl p-6 border border-gray-800">
                <h2 className="text-xl font-bold text-cyan-400 mb-6">ESTOQUE</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  {/* Quantidade Estoque */}
                  <div>
                    <label htmlFor="quantidadeEstoque" className="text-sm text-gray-400 mb-1 block">
                      Quantidade Estoque
                    </label>
                    <input 
                      id="quantidadeEstoque" 
                      name="quantidadeEstoque" 
                      type="number"
                      value={form.quantidadeEstoque}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Estoque Minimo */}
                  <div>
                    <label htmlFor="quantidadeMinima" className="text-sm text-gray-400 mb-1 block">
                      Estoque Mínimo
                    </label>
                    <input 
                      id="quantidadeMinima" 
                      name="quantidadeMinima" 
                      type="number"
                      value={form.quantidadeMinima}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Checar em Estoque */}
                  <div className="flex items-end justify-start pb-2">
                    <input 
                      id="checarEstoque" 
                      name="checarEstoque" 
                      type="checkbox"
                      checked={form.checarEstoque}
                      onChange={handleChange}
                      className="h-5 w-5 text-cyan-500 rounded border-gray-700 bg-gray-800 focus:ring-cyan-500"
                    />
                    <label htmlFor="checarEstoque" className="ml-2 text-sm text-gray-400">
                      Checar em Estoque
                    </label>
                  </div>
                </div>
              </div>
            </div>

            {/* Coluna 2: DADOS FISCAIS e PREÇO DE VENDA */}
            <div>
              {/* DADOS FISCAIS */}
              <div className="bg-gray-900/70 backdrop-blur rounded-2xl p-6 border border-gray-800 mb-6">
                <h2 className="text-xl font-bold text-cyan-400 mb-6">DADOS FISCAIS</h2>
                <div className="space-y-4">
                  {/* ICMS Percentual */}
                  <div>
                    <label htmlFor="icmsPercentual" className="text-sm text-gray-400 mb-1 block">
                      ICMS Percentual
                    </label>
                    <input 
                      id="icmsPercentual" 
                      name="icmsPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.icmsPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Reducao ICMS */}
                  <div>
                    <label htmlFor="reducaoIcmsPercentual" className="text-sm text-gray-400 mb-1 block">
                      Redução ICMS Percentual
                    </label>
                    <input 
                      id="reducaoIcmsPercentual" 
                      name="reducaoIcmsPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.reducaoIcmsPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* ICMS Substituição */}
                  <div>
                    <label htmlFor="icmsSubstituicaoPercentual" className="text-sm text-gray-400 mb-1 block">
                      ICMS Substituição Percentual
                    </label>
                    <input 
                      id="icmsSubstituicaoPercentual" 
                      name="icmsSubstituicaoPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.icmsSubstituicaoPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Diferença ICMS */}
                  <div>
                    <label htmlFor="diferencaIcmsPercentual" className="text-sm text-gray-400 mb-1 block">
                      Diferença ICMS Percentual
                    </label>
                    <input 
                      id="diferencaIcmsPercentual" 
                      name="diferencaIcmsPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.diferencaIcmsPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* IPI Percentual */}
                  <div>
                    <label htmlFor="ipiPercentual" className="text-sm text-gray-400 mb-1 block">
                      IPI Percentual
                    </label>
                    <input 
                      id="ipiPercentual" 
                      name="ipiPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.ipiPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* IPI Percentual Final */}
                  <div>
                    <label htmlFor="ipiPercentualFinal" className="text-sm text-gray-400 mb-1 block">
                      IPI Percentual Final
                    </label>
                    <input 
                      id="ipiPercentualFinal" 
                      name="ipiPercentualFinal" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.ipiPercentualFinal}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>

                  {/* Outros Percentual */}
                  <div>
                    <label htmlFor="outrosPercentual" className="text-sm text-gray-400 mb-1 block">
                      Outros Impostos (%)
                    </label>
                    <input 
                      id="outrosPercentual" 
                      name="outrosPercentual" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.outrosPercentual}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                    <p className="text-xs text-cyan-400 mt-1">Usado no cálculo do custo médio</p>
                  </div>
                </div>
              </div>

              {/* PREÇO DE VENDA */}
              <div className="bg-gray-900/70 backdrop-blur rounded-2xl p-6 border border-gray-800">
                <h2 className="text-xl font-bold text-cyan-400 mb-6">PREÇO DE VENDA</h2>
                <div className="space-y-4">
                  {/* Preço Produto (Compra) */}
                  <div>
                    <label htmlFor="precoProduto" className="text-sm text-gray-400 mb-1 block">
                      Preço Compra
                    </label>
                    <input 
                      id="precoProduto" 
                      name="precoProduto" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.precoProduto}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                  </div>
                  
                  {/* Margem de Lucro */}
                  <div>
                    <label htmlFor="margemLucro" className="text-sm text-gray-400 mb-1 block">
                      Margem de Lucro (%)
                    </label>
                    <input 
                      id="margemLucro" 
                      name="margemLucro" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.margemLucro}
                      onChange={handleChange}
                      className="w-full bg-gray-800/50 border border-gray-700 rounded-lg px-4 py-3 text-white focus:border-cyan-500 outline-none"
                    />
                    <p className="text-xs text-cyan-400 mt-1">Define o lucro sobre o custo médio</p>
                  </div>

                  {/* Custo Medio - CALCULADO */}
                  <div>
                    <label htmlFor="custoMedio" className="text-sm text-gray-400 mb-1 block">
                      Custo Médio (Calculado)
                    </label>
                    <input 
                      id="custoMedio" 
                      name="custoMedio" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.custoMedio}
                      readOnly
                      className="w-full bg-gray-700/50 border border-cyan-500/30 rounded-lg px-4 py-3 text-cyan-400 font-bold cursor-not-allowed"
                    />
                    <p className="text-xs text-gray-500 mt-1">Preço + Impostos (auto)</p>
                  </div>
                  
                  {/* Preço de Venda - CALCULADO */}
                  <div>
                    <label htmlFor="precoVenda" className="text-sm text-gray-400 mb-1 block">
                      Preço de Venda (Calculado)
                    </label>
                    <input 
                      id="precoVenda" 
                      name="precoVenda" 
                      type="number" 
                      step="0.01" 
                      placeholder="0.00"
                      value={form.precoVenda}
                      readOnly
                      className="w-full bg-gray-700/50 border border-green-500/30 rounded-lg px-4 py-3 text-green-400 font-bold text-lg cursor-not-allowed"
                    />
                    <p className="text-xs text-gray-500 mt-1">Custo Médio + Margem (auto)</p>
                  </div>
                  
                  {/* Último Reajuste - AUTOMÁTICO */}
                  <div>
                    <label htmlFor="ultimoReajuste" className="text-sm text-gray-400 mb-1 block">
                      Último Reajuste
                    </label>
                    <input 
                      id="ultimoReajuste" 
                      name="ultimoReajuste" 
                      type="date"
                      value={form.ultimoReajuste}
                      onChange={handleChange}
                      className="w-full bg-gray-700/30 border border-gray-600 rounded-lg px-4 py-3 text-gray-400 cursor-not-allowed"
                      readOnly
                    />
                    <p className="text-xs text-yellow-400 mt-1">
                      {isEdit ? '⚡ Atualiza automaticamente ao mudar preços' : '📅 Será definida ao cadastrar'}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Botões de Ação */}
          <div className="flex gap-4 justify-end">
            <button 
              type="button" 
              onClick={() => navigate('/produtos')} 
              disabled={loading}
              className="px-8 py-3 bg-gray-800 hover:bg-gray-700 text-white font-bold rounded-xl transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              CANCELAR
            </button>
            <button 
              type="submit" 
              disabled={loading}
              className="px-8 py-3 bg-cyan-500 hover:bg-cyan-400 text-black font-bold rounded-xl transition flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <Save className="w-5 h-5" />
              {loading ? 'SALVANDO...' : (isEdit ? 'ATUALIZAR' : 'SALVAR')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}