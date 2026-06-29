using Clprolf.ArchUnitNet.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace Clprolf.Examples.WPF.Agents.Impl.SystemOriented
{
   
    [ClSystem]
    public class RelayCommand : ICommand
    {
        private readonly Action<object?> _execute;
        private readonly Predicate<object?>? _canExecute;

        /// <summary>
        /// Événement requis par ICommand. Il prévient le bouton WPF 
        /// quand l'état du "CanExecute" change (pour griser/dégriser le bouton).
        /// </summary>
        public event EventHandler? CanExecuteChanged
        {
            add => CommandManager.RequerySuggested += value;
            remove => CommandManager.RequerySuggested -= value;
        }

        // Constructeur pour les commandes qui s'exécutent toujours (sans condition)
        public RelayCommand(Action<object?> execute) : this(execute, null)
        {
        }

        // Constructeur complet avec condition d'exécution
        public RelayCommand(Action<object?> execute, Predicate<object?>? canExecute)
        {
            _execute = execute ?? throw new ArgumentNullException(nameof(execute));
            _canExecute = canExecute;
        }

        /// <summary>
        /// Détermine si la commande peut s'exécuter dans l'état actuel.
        /// </summary>
        public bool CanExecute(object? parameter)
        {
            return _canExecute == null || _canExecute(parameter);
        }

        /// <summary>
        /// Exécute l'action technique déléguée par l'Agent.
        /// </summary>
        public void Execute(object? parameter)
        {
            _execute(parameter);
        }
    }
}
