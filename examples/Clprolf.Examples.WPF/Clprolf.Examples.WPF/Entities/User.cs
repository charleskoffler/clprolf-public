using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.WPF.Agents.Traits.SystemOriented;
using System.ComponentModel;

namespace Clprolf.Examples.WPF.Entities
{
    /// <summary>
    /// Represents a user entity within the system.
    /// It is classified as an Agent because it carries an identity and a strong conceptual meaning.
    /// </summary>
    ///
    [ClInterfaceBypass]
    [ClAgent]
    public class User : IClNotifyPropertyChanged
    {
        public event PropertyChangedEventHandler? PropertyChanged;

        private string _name = string.Empty;
        public string Name
        {
            get => _name;
            set
            {
                if (_name != value)
                {
                    _name = value;
                    PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(nameof(Name)));
                }
            }
        }

        public int Id { get; internal set; }

        public User() { }
        public User(string name) => Name = name;
    }
}