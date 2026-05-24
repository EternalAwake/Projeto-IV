// Exclusão da música na visualização
document.querySelectorAll('.excluir-musica').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const musicaId = this.dataset.musicaId;
        const musicaNome = this.dataset.musicaNome;

        if (confirm(`Tem certeza que deseja excluir a música "${musicaNome}"?`)) {
            fetch(`/biblioteca/musicas/visualizar/excluir/${musicaId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Música excluída com sucesso.');
                    alert('Música excluída com sucesso!');
                    // Redireciona para a listagem de músicas
                    window.location.href = '/biblioteca/musicas';
                } else {
                    console.error('Erro ao excluir música. Status:', response.status);
                    alert('Erro ao excluir música. Tente novamente.');
                }
            })
            .catch(error => {
                console.error('Erro de rede:', error);
                alert('Erro de rede ao excluir música.');
            });
        }
    });
});