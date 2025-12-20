import { useState, useEffect } from 'react';
import { ArrowLeft, Save, Plus, Trash2, Car, AlertCircle, Check, RefreshCw } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../services/api';

export default function ClientFormPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEdit = !!id;

    const [cliente, setCliente] = useState({
        nome: '', cpf: '', telefone: '', email: '',
        cep: '', rua: '', numero: '', bairro: '', cidade: '', estado: ''
    });

    const [veiculos, setVeiculos] = useState([{
        placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
    }]);

    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    // Carrega dados em modo de edição
    useEffect(() => {
        if (isEdit) {
            carregarDados();
        }
    }, [id, isEdit]);

    const carregarDados = async () => {
        try {
            setLoading(true);
            
            // Carrega cliente
            const resCliente = await api.get(`/clientes/${id}`);
            console.log('✅ Cliente carregado:', resCliente.data);
            setCliente(resCliente.data);

            // Verifica se o cliente já tem veículos no próprio objeto
            if (resCliente.data.veiculos && resCliente.data.veiculos.length > 0) {
                console.log('✅ Veículos encontrados no objeto cliente:', resCliente.data.veiculos);
                setVeiculos(resCliente.data.veiculos);
                setLoading(false);
                return;
            }

            // Tenta carregar veículos por endpoint separado
            console.log('🔍 Buscando veículos no endpoint: /veiculos/cliente/' + id);
            try {
                const resVeiculos = await api.get(`/veiculos/cliente/${id}`);
                console.log('📦 Resposta completa de veículos:', resVeiculos);
                console.log('📋 Data de veículos:', resVeiculos.data);
                console.log('📊 Tipo de data:', typeof resVeiculos.data, Array.isArray(resVeiculos.data));
                
                if (resVeiculos.data) {
                    // Verifica se é um array ou objeto com array dentro
                    let veiculosArray = Array.isArray(resVeiculos.data) 
                        ? resVeiculos.data 
                        : resVeiculos.data.veiculos || resVeiculos.data.content || [];
                    
                    console.log('🚗 Array de veículos processado:', veiculosArray);
                    
                    if (veiculosArray.length > 0) {
                        console.log('✅ ' + veiculosArray.length + ' veículo(s) carregado(s)');
                        setVeiculos(veiculosArray);
                    } else {
                        console.log('⚠️ Array de veículos vazio');
                        setVeiculos([{
                            placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
                        }]);
                    }
                } else {
                    console.log('⚠️ resVeiculos.data é null/undefined');
                    setVeiculos([{
                        placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
                    }]);
                }
            } catch (errVeiculos) {
                console.error('❌ Erro ao carregar veículos:', errVeiculos);
                console.log('📍 Tentando endpoint alternativo: /clientes/' + id + '/veiculos');
                
                // Tenta endpoint alternativo
                try {
                    const resVeiculosAlt = await api.get(`/clientes/${id}/veiculos`);
                    console.log('📦 Resposta alternativa:', resVeiculosAlt.data);
                    
                    let veiculosArray = Array.isArray(resVeiculosAlt.data) 
                        ? resVeiculosAlt.data 
                        : resVeiculosAlt.data.veiculos || [];
                    
                    if (veiculosArray.length > 0) {
                        console.log('✅ Veículos carregados do endpoint alternativo');
                        setVeiculos(veiculosArray);
                    } else {
                        console.log('⚠️ Endpoint alternativo retornou vazio');
                        setVeiculos([{
                            placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
                        }]);
                    }
                } catch (errAlt) {
                    console.error('❌ Endpoint alternativo também falhou:', errAlt);
                    console.log('ℹ️ Criando veículo vazio');
                    setVeiculos([{
                        placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
                    }]);
                }
            }
        } catch (err) {
            console.error('❌ Erro ao carregar cliente:', err);
            alert('Cliente não encontrado');
            navigate('/clientes');
        } finally {
            setLoading(false);
        }
    };

    const adicionarVeiculo = () => {
        setVeiculos([...veiculos, {
            placa: '', ano: '', marca: '', modelo: '', cor: '', observacoes: ''
        }]);
    };


const removerVeiculo = async (index) => {
    if (veiculos.length === 1) {
        alert('É necessário ter pelo menos um veículo cadastrado');
        return;
    }

    const veiculo = veiculos[index];

    if (veiculo.id) {
        const confirmar = window.confirm(
            `Tem certeza que deseja excluir o veículo ${veiculo.placa || veiculo.modelo || 'sem placa'}?`
        );

        if (!confirmar) return;

        try {
            await api.delete(`/veiculos/${veiculo.id}`);
            setVeiculos(veiculos.filter((_, i) => i !== index));
            alert('Veículo excluído com sucesso!');
        } catch (err) {
            console.error('Erro ao deletar veículo:', err);
            alert(
                'Erro ao excluir veículo: ' +
                (err.response?.data?.message || err.message || 'Tente novamente')
            );
            return; 
        }
    } else {
        // Veículo novo (ainda não salvo) → só remove da tela
        setVeiculos(veiculos.filter((_, i) => i !== index));
    }
};

   /* const removerVeiculo = (index) => {
        if (veiculos.length === 1) {
            alert('É necessário ter pelo menos um veículo cadastrado');
            return;
        }
        setVeiculos(veiculos.filter((_, i) => i !== index));
    };
*/
    const atualizarVeiculo = (index, campo, valor) => {
        const novos = [...veiculos];
        novos[index][campo] = valor;
        setVeiculos(novos);
    };

    const salvarVeiculo = async (index) => {
        const v = veiculos[index];
        
        if (!cliente.id && !id) {
            alert('Salve o cliente primeiro antes de adicionar veículos');
            return;
        }

        if (!v.placa && !v.modelo) {
            alert('Preencha pelo menos a placa ou modelo do veículo');
            return;
        }

        try {
            const clienteId = id || cliente.id;
            const veiculoData = {
                ...v,
                cliente: { id: clienteId }
            };

            if (v.id) {
                await api.put(`/veiculos/${v.id}`, veiculoData);
                alert('Veículo atualizado com sucesso!');
            } else {
                const res = await api.post('/veiculos', veiculoData);
                const novosVeiculos = [...veiculos];
                novosVeiculos[index] = { ...v, id: res.data.id };
                setVeiculos(novosVeiculos);
                alert('Veículo salvo com sucesso!');
            }
        } catch (err) {
            console.error(err);
            alert('Erro ao salvar veículo: ' + (err.response?.data?.message || err.message));
        }
    };

    // Máscaras simples aplicadas no onChange
    const aplicarMascaraCPF = (valor) => {
        return valor
            .replace(/\D/g, '')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d{1,2})/, '$1-$2')
            .replace(/(-\d{2})\d+?$/, '$1');
    };

    const aplicarMascaraTelefone = (valor) => {
        return valor
            .replace(/\D/g, '')
            .replace(/(\d{2})(\d)/, '($1) $2')
            .replace(/(\d{5})(\d)/, '$1-$2')
            .replace(/(-\d{4})\d+?$/, '$1');
    };

    const aplicarMascaraCEP = (valor) => {
        return valor
            .replace(/\D/g, '')
            .replace(/(\d{5})(\d)/, '$1-$2')
            .replace(/(-\d{3})\d+?$/, '$1');
    };

    const aplicarMascaraPlaca = (valor) => {
        return valor
            .toUpperCase()
            .replace(/[^A-Z0-9]/g, '')
            .replace(/^([A-Z]{3})([0-9])/, '$1-$2')
            .substring(0, 8);
    };

    const validarFormulario = () => {
        const newErrors = {};

        if (!cliente.nome?.trim()) {
            newErrors.nome = 'Nome é obrigatório';
        }

        const veiculosValidos = veiculos.filter(v => v.placa || v.modelo);
        if (veiculosValidos.length === 0) {
            newErrors.veiculos = 'Adicione pelo menos um veículo com placa ou modelo';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!validarFormulario()) {
            return;
        }

        setLoading(true);

        try {
            let clienteId = id;

            if (isEdit) {
                await api.put(`/clientes/${id}`, cliente);
            } else {
                const res = await api.post('/clientes', cliente);
                clienteId = res.data.id;
                setCliente({ ...cliente, id: clienteId });
            }

            // Salva veículos que ainda não têm ID
            for (let i = 0; i < veiculos.length; i++) {
                const v = veiculos[i];
                if ((v.placa || v.modelo) && !v.id) {
                    const veiculoData = {
                        ...v,
                        cliente: { id: clienteId }
                    };
                    const res = await api.post('/veiculos', veiculoData);
                    veiculos[i] = { ...v, id: res.data.id };
                }
            }

            alert(isEdit ? 'Cliente atualizado!' : 'Cliente cadastrado com sucesso!');
            navigate('/clientes');
        } catch (err) {
            console.error(err);
            alert('Erro: ' + (err.response?.data?.message || err.message));
        } finally {
            setLoading(false);
        }
    };

    if (loading && isEdit) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-cyan-500 mx-auto mb-4"></div>
                    <p className="text-2xl text-cyan-400">Carregando cliente...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-black p-4 lg:p-6">
            <div className="max-w-7xl mx-auto">

                {/* HEADER */}
                <div className="flex items-center gap-4 lg:gap-6 mb-8 lg:mb-10">
                    <button
                        onClick={() => navigate('/clientes')}
                        className="p-3 lg:p-4 bg-gray-800 rounded-xl lg:rounded-2xl hover:bg-gray-700 transition shadow-lg"
                    >
                        <ArrowLeft className="w-6 h-6 lg:w-8 lg:h-8 text-white" />
                    </button>
                    <h1 className="text-2xl lg:text-4xl font-extrabold text-white flex items-center gap-3 lg:gap-4">
                        <Car className="w-8 h-8 lg:w-12 lg:h-12 text-cyan-400" />
                        {isEdit ? 'EDITAR CLIENTE' : 'NOVO CLIENTE'}
                    </h1>
                </div>

                {/* Erros */}
                {Object.keys(errors).length > 0 && (
                    <div className="bg-red-900/30 border border-red-500 rounded-xl p-4 mb-6 flex items-start gap-3">
                        <AlertCircle className="w-6 h-6 text-red-400 flex-shrink-0" />
                        <div>
                            <p className="font-bold text-red-400 mb-2">Corrija os seguintes erros:</p>
                            <ul className="list-disc list-inside text-red-300 space-y-1">
                                {Object.entries(errors).map(([key, msg]) => (
                                    <li key={key}>{msg}</li>
                                ))}
                            </ul>
                        </div>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-8 lg:space-y-10">

                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 lg:gap-10">

                        {/* COLUNA ESQUERDA */}
                        <div className="space-y-6 lg:space-y-8">

                            {/* DADOS PESSOAIS */}
                            <div className="bg-gray-900/80 backdrop-blur-lg rounded-2xl lg:rounded-3xl p-6 lg:p-8 border border-gray-700 shadow-2xl">
                                <h2 className="text-xl lg:text-2xl font-bold text-cyan-400 mb-6 lg:mb-8">DADOS DO CLIENTE</h2>
                                <div className="space-y-4 lg:space-y-6">
                                    <div>
                                        <input
                                            required
                                            placeholder="Nome Completo *"
                                            value={cliente.nome}
                                            onChange={e => setCliente({ ...cliente, nome: e.target.value })}
                                            className={`w-full bg-gray-800/70 border ${errors.nome ? 'border-red-500' : 'border-gray-700'} rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none transition text-white`}
                                        />
                                        {errors.nome && <p className="text-red-400 text-sm mt-1">{errors.nome}</p>}
                                    </div>

                                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 lg:gap-6">
                                        <input
                                            placeholder="CPF (000.000.000-00)"
                                            value={cliente.cpf}
                                            onChange={e => setCliente({ ...cliente, cpf: aplicarMascaraCPF(e.target.value) })}
                                            maxLength={14}
                                            className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                        <input
                                            placeholder="Telefone (00) 00000-0000"
                                            value={cliente.telefone}
                                            onChange={e => setCliente({ ...cliente, telefone: aplicarMascaraTelefone(e.target.value) })}
                                            maxLength={15}
                                            className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                    </div>

                                    <input
                                        type="email"
                                        placeholder="E-mail"
                                        value={cliente.email}
                                        onChange={e => setCliente({ ...cliente, email: e.target.value })}
                                        className="w-full bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                    />
                                </div>
                            </div>

                            {/* ENDEREÇO */}
                            <div className="bg-gray-900/80 backdrop-blur-lg rounded-2xl lg:rounded-3xl p-6 lg:p-8 border border-gray-700 shadow-2xl">
                                <h2 className="text-xl lg:text-2xl font-bold text-cyan-400 mb-6 lg:mb-8">ENDEREÇO</h2>
                                <div className="space-y-4 lg:space-y-6">
                                    <div className="grid grid-cols-3 gap-3 lg:gap-6">
                                        <input
                                            placeholder="CEP"
                                            value={cliente.cep}
                                            onChange={e => setCliente({ ...cliente, cep: aplicarMascaraCEP(e.target.value) })}
                                            maxLength={9}
                                            className="bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                        <input
                                            placeholder="Rua"
                                            value={cliente.rua}
                                            onChange={e => setCliente({ ...cliente, rua: e.target.value })}
                                            className="col-span-2 bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                    </div>

                                    <div className="grid grid-cols-3 gap-3 lg:gap-6">
                                        <input
                                            placeholder="Número"
                                            value={cliente.numero}
                                            onChange={e => setCliente({ ...cliente, numero: e.target.value })}
                                            className="bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                        <input
                                            placeholder="Bairro"
                                            value={cliente.bairro}
                                            onChange={e => setCliente({ ...cliente, bairro: e.target.value })}
                                            className="col-span-2 bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                    </div>

                                    <div className="grid grid-cols-3 gap-3 lg:gap-6">
                                        <input
                                            placeholder="Cidade"
                                            value={cliente.cidade}
                                            onChange={e => setCliente({ ...cliente, cidade: e.target.value })}
                                            className="col-span-2 bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white"
                                        />
                                        <input
                                            placeholder="UF"
                                            maxLength={2}
                                            value={cliente.estado}
                                            onChange={e => setCliente({ ...cliente, estado: e.target.value.toUpperCase() })}
                                            className="bg-gray-800/70 border border-gray-700 rounded-xl px-4 lg:px-6 py-3 lg:py-4 text-base lg:text-lg focus:border-cyan-500 outline-none text-white uppercase"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* COLUNA DIREITA: VEÍCULOS */}
                        <div className="bg-gray-900/80 backdrop-blur-lg rounded-2xl lg:rounded-3xl p-6 lg:p-8 border border-gray-700 shadow-2xl">
                            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6 lg:mb-8">
                                <h2 className="text-xl lg:text-2xl font-bold text-cyan-400">
                                    VEÍCULOS 
                                    <span className="text-sm text-gray-400 ml-2">
                                        ({veiculos.filter(v => v.id).length} salvos)
                                    </span>
                                </h2>
                                <div className="flex gap-2">
                                    {isEdit && (
                                        <button
                                            type="button"
                                            onClick={carregarDados}
                                            className="bg-blue-600 hover:bg-blue-500 text-white font-bold py-2 lg:py-3 px-4 lg:px-6 rounded-xl flex items-center gap-2 transition shadow-lg text-sm lg:text-base"
                                            title="Recarregar veículos"
                                        >
                                            <RefreshCw className="w-5 h-5 lg:w-6 lg:h-6" />
                                            Recarregar
                                        </button>
                                    )}
                                    <button
                                        type="button"
                                        onClick={adicionarVeiculo}
                                        className="bg-cyan-600 hover:bg-cyan-500 text-black font-bold py-2 lg:py-3 px-4 lg:px-6 rounded-xl flex items-center gap-2 lg:gap-3 transition shadow-lg text-sm lg:text-base"
                                    >
                                        <Plus className="w-5 h-5 lg:w-6 lg:h-6" />
                                        Adicionar
                                    </button>
                                </div>
                            </div>

                            <div className="space-y-4 lg:space-y-6 max-h-[500px] overflow-y-auto pr-2">
                                {veiculos.map((v, i) => (
                                    <div key={i} className="bg-gray-800/60 rounded-xl lg:rounded-2xl p-4 lg:p-6 border border-gray-700">
                                        <div className="flex justify-between items-center mb-4">
                                            <h3 className="text-lg lg:text-xl font-bold text-cyan-300">
                                                Veículo #{i + 1}
                                                {v.id && (
                                                    <span className="text-xs text-green-400 ml-2">
                                                        ✓ Salvo (ID: {v.id})
                                                    </span>
                                                )}
                                            </h3>
                                            <div className="flex gap-2">
                                                <button
                                                    type="button"
                                                    onClick={() => salvarVeiculo(i)}
                                                    className="bg-green-600 hover:bg-green-500 text-white font-bold py-2 px-4 rounded-lg flex items-center gap-2 transition text-sm"
                                                    title="Salvar veículo"
                                                >
                                                    <Check className="w-4 h-4" />
                                                    {v.id ? 'Atualizar' : 'Salvar'}
                                                </button>
                                                {veiculos.length > 1 && (
                                                    <button
                                                        type="button"
                                                        onClick={() => removerVeiculo(i)}
                                                        className="text-red-400 hover:text-red-300 p-2 hover:bg-red-900/30 rounded transition"
                                                        title="Remover veículo"
                                                    >
                                                        <Trash2 className="w-5 h-5 lg:w-6 lg:h-6" />
                                                    </button>
                                                )}
                                            </div>
                                        </div>

                                        <div className="grid grid-cols-2 gap-3 lg:gap-6">
                                            <input
                                                placeholder="Placa (ABC-1D23)"
                                                value={v.placa}
                                                onChange={e => atualizarVeiculo(i, 'placa', aplicarMascaraPlaca(e.target.value))}
                                                maxLength={8}
                                                className="bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none text-white uppercase"
                                            />
                                            <input
                                                type="number"
                                                placeholder="Ano"
                                                value={v.ano}
                                                onChange={e => atualizarVeiculo(i, 'ano', e.target.value)}
                                                className="bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none text-white"
                                            />
                                        </div>

                                        <div className="grid grid-cols-2 gap-3 lg:gap-6 mt-3 lg:mt-4">
                                            <input
                                                placeholder="Marca"
                                                value={v.marca}
                                                onChange={e => atualizarVeiculo(i, 'marca', e.target.value)}
                                                className="bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none text-white"
                                            />
                                            <input
                                                placeholder="Modelo"
                                                value={v.modelo}
                                                onChange={e => atualizarVeiculo(i, 'modelo', e.target.value)}
                                                className="bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none text-white"
                                            />
                                        </div>

                                        <input
                                            placeholder="Cor"
                                            value={v.cor}
                                            onChange={e => atualizarVeiculo(i, 'cor', e.target.value)}
                                            className="w-full mt-3 lg:mt-4 bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none text-white"
                                        />

                                        <textarea
                                            placeholder="Observações"
                                            rows={2}
                                            value={v.observacoes}
                                            onChange={e => atualizarVeiculo(i, 'observacoes', e.target.value)}
                                            className="w-full mt-3 lg:mt-4 bg-gray-700/70 border border-gray-600 rounded-xl px-4 lg:px-6 py-3 lg:py-4 focus:border-cyan-500 outline-none resize-none text-white"
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                    {/* BOTÕES FINAIS */}
                    <div className="flex flex-col sm:flex-row justify-center gap-4 lg:gap-10 pt-6 lg:pt-10">
                        <button
                            type="button"
                            onClick={() => navigate('/clientes')}
                            className="px-12 lg:px-16 py-4 lg:py-5 bg-red-600 hover:bg-red-500 text-white text-lg lg:text-xl font-bold rounded-xl lg:rounded-2xl transition shadow-2xl"
                        >
                            CANCELAR
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="px-12 lg:px-16 py-4 lg:py-5 bg-cyan-500 hover:bg-cyan-400 text-black text-lg lg:text-xl font-bold rounded-xl lg:rounded-2xl transition shadow-2xl flex items-center justify-center gap-3 lg:gap-4 disabled:opacity-60 disabled:cursor-not-allowed"
                        >
                            <Save className="w-6 h-6 lg:w-8 lg:h-8" />
                            {loading ? 'SALVANDO...' : 'SALVAR CLIENTE'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}